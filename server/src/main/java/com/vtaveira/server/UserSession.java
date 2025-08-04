package com.vtaveira.server;

import com.vtaveira.domain.model.UserStatus;
import lombok.Data;

@Data
public class UserSession {
  private String username;
  private String ipAddress;
  private UserStatus status = UserStatus.OFFLINE;
}
