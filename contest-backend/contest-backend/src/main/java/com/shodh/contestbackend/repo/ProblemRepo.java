package com.shodh.contestbackend.repo;

import com.shodh.contestbackend.model.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepo extends JpaRepository<Problem, Long> {}
