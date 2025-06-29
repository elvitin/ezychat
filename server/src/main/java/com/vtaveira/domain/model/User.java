package com.vtaveira.domain.model;

import lombok.Data;

@Data
public class User {
  private long id;
  private String username;
  private String fullName;
  private String email;
  private String password;
  private UserStatus status;

  public User() {
  }

  public User(String username, String fullName, String email, String password) {
    this.username = username;
    this.fullName = fullName;
    this.email = email;
    this.password = password;
    this.status = UserStatus.OFFLINE; // Default status
  }
}
