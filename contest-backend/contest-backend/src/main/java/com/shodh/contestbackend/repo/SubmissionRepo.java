package com.shodh.contestbackend.repo;

import com.shodh.contestbackend.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubmissionRepo extends JpaRepository<Submission, Long> {
    List<Submission> findByContestId(Long contestId);
}
