package com.shodh.contestbackend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Contest {
    @Id @GeneratedValue
    private Long id;
    private String name;

    @OneToMany(mappedBy = "contest", cascade = CascadeType.ALL)
    private List<Problem> problems;
}
