package com.shodh.contestbackend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "app_user")   // ✅ avoid reserved word USER
@Data
public class User {
  @Id @GeneratedValue
  private Long id;
  private String username;
}
