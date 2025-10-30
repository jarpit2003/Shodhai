package com.shodh.contestbackend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;

@Entity
@Data
public class Submission {
    @Id @GeneratedValue
    private Long id;

    private Long contestId;
    private Long problemId;
    private Long userId;

    private Instant createdAt = Instant.now();

    @Enumerated(EnumType.STRING)
    private SubmissionStatus status = SubmissionStatus.PENDING;

    @Lob private String sourceCode;
    @Lob private String compileStderr;
    @Lob private String runStdout;
    @Lob private String runStderr;
    private Long timeMs;
}
