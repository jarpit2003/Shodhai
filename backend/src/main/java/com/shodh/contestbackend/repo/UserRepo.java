package com.shodh.contestbackend.repo;

import com.shodh.contestbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {}
