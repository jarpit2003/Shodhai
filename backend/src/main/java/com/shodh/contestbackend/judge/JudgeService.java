package com.shodh.contestbackend.judge;

import com.shodh.contestbackend.model.Submission;
import com.shodh.contestbackend.model.SubmissionStatus;
import com.shodh.contestbackend.repo.ProblemRepo;
import com.shodh.contestbackend.repo.SubmissionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JudgeService {

    private final SubmissionRepo submissionRepo;
    private final ProblemRepo problemRepo;

    @Value("${judge.compileTimeoutMs:8000}")
    private long compileTimeoutMs;

    @Value("${judge.runTimeoutMs:3000}")
    private long runTimeoutMs;

    public void process(Long submissionId) throws Exception {
        Submission s = submissionRepo.findById(submissionId).orElseThrow();
        s.setStatus(SubmissionStatus.RUNNING);
        submissionRepo.save(s);

        var problem = problemRepo.findById(s.getProblemId()).orElseThrow();

        Path work = Files.createTempDirectory("judge-" + submissionId + "-");
        try {
            // write source file
            Path mainJava = work.resolve("Main.java");
            Files.writeString(mainJava, s.getSourceCode(), StandardCharsets.UTF_8);

            // --- compile ---
            ProcessBuilder pbCompile = new ProcessBuilder("javac", "Main.java");
            pbCompile.directory(work.toFile());
            pbCompile.redirectErrorStream(true);
            Process pCompile = pbCompile.start();

            boolean compiled = pCompile.waitFor(compileTimeoutMs, TimeUnit.MILLISECONDS);
            String compileOut = new String(pCompile.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            s.setCompileStderr(compileOut);

            if (!compiled || pCompile.exitValue() != 0) {
                s.setStatus(SubmissionStatus.COMPILE_ERROR);
                submissionRepo.save(s);
                return;
            }

            // --- run ---
            long start = System.currentTimeMillis();
            ProcessBuilder pbRun = new ProcessBuilder("java", "Main");
            pbRun.directory(work.toFile());
            // separate streams; we will read both
            Process pRun = pbRun.start();

            // feed official input to STDIN
            try (BufferedWriter w = new BufferedWriter(new OutputStreamWriter(pRun.getOutputStream(), StandardCharsets.UTF_8))) {
                w.write(problem.getOfficialInput() == null ? "" : problem.getOfficialInput());
                w.flush();
            } catch (IOException ignore) {}

            boolean finished = pRun.waitFor(runTimeoutMs, TimeUnit.MILLISECONDS);
            if (!finished) {
                pRun.destroyForcibly();
                s.setStatus(SubmissionStatus.TLE);
                submissionRepo.save(s);
                return;
            }

            String stdout = new String(pRun.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();
            String stderr = new String(pRun.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);

            s.setRunStdout(stdout);
            s.setRunStderr(stderr);
            s.setTimeMs(System.currentTimeMillis() - start);

            String expected = problem.getOfficialOutput() == null ? "" : problem.getOfficialOutput().trim();
            if (stdout.equals(expected)) {
                s.setStatus(SubmissionStatus.ACCEPTED);
            } else {
                s.setStatus(SubmissionStatus.WRONG_ANSWER);
            }
            submissionRepo.save(s);

        } catch (Exception ex) {
            s.setStatus(SubmissionStatus.RUNTIME_ERROR);
            s.setRunStderr((ex.getClass().getSimpleName() + ": " + ex.getMessage()));
            submissionRepo.save(s);
        } finally {
            // cleanup temp dir
            try {
                Files.walk(work)
                        .sorted(Comparator.reverseOrder())
                        .forEach(p -> { try { Files.deleteIfExists(p);} catch (Exception ignored){} });
            } catch (Exception ignored) {}
        }
    }
}
