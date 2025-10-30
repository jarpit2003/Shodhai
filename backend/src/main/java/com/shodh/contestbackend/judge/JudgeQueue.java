package com.shodh.contestbackend.judge;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
@RequiredArgsConstructor
public class JudgeQueue {
    private final BlockingQueue<Long> queue = new LinkedBlockingQueue<>();
    private final JudgeService judgeService;

    public void enqueue(Long submissionId) {
        queue.offer(submissionId);
    }

    @PostConstruct
    void startWorker() {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    Long id = queue.take();
                    judgeService.process(id);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "judge-worker-1");
        t.setDaemon(true);
        t.start();
    }
}
