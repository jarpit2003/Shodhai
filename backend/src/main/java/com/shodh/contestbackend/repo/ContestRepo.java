package com.shodh.contestbackend.repo;

import com.shodh.contestbackend.model.Contest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContestRepo extends JpaRepository<Contest, Long> {}
