package com.shodh.contestbackend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Problem {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Contest contest;

    private String title;

    @Lob private String statement;
    @Lob private String sampleInput;
    @Lob private String sampleOutput;

    @Lob private String officialInput;
    @Lob private String officialOutput;
}
