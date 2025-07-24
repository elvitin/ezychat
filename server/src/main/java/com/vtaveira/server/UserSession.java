package com.vtaveira.server;

import com.vtaveira.domain.model.UserStatus;
import lombok.Data;

@Data
public class UserSession {
  private UserStatus status = UserStatus.OFFLINE;
  private String username;
  private String ipAddress;
}
