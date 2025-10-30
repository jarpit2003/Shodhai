package com.shodh.contestbackend.controller;

import com.shodh.contestbackend.judge.JudgeQueue;   // ✅ add this import

import com.shodh.contestbackend.model.Submission;
import com.shodh.contestbackend.model.SubmissionStatus;
import com.shodh.contestbackend.repo.SubmissionRepo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SubmissionController {

  private final SubmissionRepo submissionRepo;
  private final JudgeQueue judgeQueue;  // ✅ enqueue worker

  @PostMapping("/submissions")
  public ResponseEntity<SubmitResponse> submit(@Valid @RequestBody SubmitRequest req) {
    Submission s = new Submission();
    s.setContestId(req.getContestId());
    s.setProblemId(req.getProblemId());
    s.setUserId(req.getUserId());
    s.setSourceCode(req.getSourceCode());
    s.setStatus(SubmissionStatus.PENDING);

    submissionRepo.save(s);

    judgeQueue.enqueue(s.getId());   // ✅ SUBMISSION NOW GOES TO JUDGE QUEUE

    return ResponseEntity.ok(new SubmitResponse(s.getId()));
  }

  @GetMapping("/submissions/{id}")
  public ResponseEntity<Submission> get(@PathVariable Long id) {
    return submissionRepo.findById(id)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  // ---- DTOs (explicit getters/setters, no Lombok) ----
  public static class SubmitRequest {
    @NotNull private Long contestId;
    @NotNull private Long problemId;
    @NotNull private Long userId;
    @NotBlank private String sourceCode;

    public Long getContestId() { return contestId; }
    public void setContestId(Long contestId) { this.contestId = contestId; }

    public Long getProblemId() { return problemId; }
    public void setProblemId(Long problemId) { this.problemId = problemId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getSourceCode() { return sourceCode; }
    public void setSourceCode(String sourceCode) { this.sourceCode = sourceCode; }
  }

  public static class SubmitResponse {
    private final Long submissionId;
    public SubmitResponse(Long submissionId) { this.submissionId = submissionId; }
    public Long getSubmissionId() { return submissionId; }
  }
}
