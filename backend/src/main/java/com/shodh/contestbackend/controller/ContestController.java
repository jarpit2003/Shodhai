package com.shodh.contestbackend.controller;

import com.shodh.contestbackend.repo.ContestRepo;
import com.shodh.contestbackend.repo.SubmissionRepo;
import com.shodh.contestbackend.repo.UserRepo;
import com.shodh.contestbackend.model.SubmissionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/contests")
@RequiredArgsConstructor
public class ContestController {

  private final ContestRepo contestRepo;
  private final SubmissionRepo submissionRepo;
  private final UserRepo userRepo;

  @GetMapping("/{id}")
  public ResponseEntity<?> getContest(@PathVariable Long id) {
    return contestRepo.findById(id)
      .<ResponseEntity<?>>map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/{id}/leaderboard")
  public List<Map<String, Object>> leaderboard(@PathVariable Long id) {

    var submissions = submissionRepo.findByContestId(id);

    Map<Long, Long> solvedCount = new HashMap<>();
    Map<Long, Long> bestTime = new HashMap<>();

    for (var s : submissions) {
      if (s.getStatus() == SubmissionStatus.ACCEPTED) {
        // increase solved count
        solvedCount.merge(s.getUserId(), 1L, Long::sum);

        // best execution time wins (lower time is better)
        long t = (s.getTimeMs() == null ? 999_999 : s.getTimeMs());
        bestTime.merge(s.getUserId(), t, Math::min);
      }
    }

    return solvedCount.entrySet().stream()
      .sorted(
        Comparator
          .<Map.Entry<Long, Long>>comparingLong(e -> -e.getValue())   // sort by solved desc
          .thenComparing(e -> bestTime.getOrDefault(e.getKey(), 999_999L)) // then by time asc
      )
      .map(e -> Map.<String, Object>of(
        "userId", e.getKey(),
        "username", userRepo.findById(e.getKey()).map(u -> u.getUsername()).orElse("user-" + e.getKey()),
        "solved", e.getValue(),
        "bestTimeMs", bestTime.getOrDefault(e.getKey(), 0L)
      ))
      .toList();
  }
}
