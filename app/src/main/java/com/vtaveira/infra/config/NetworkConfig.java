package com.vtaveira.infra.config;

import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class NetworkConfig {

  public String getServerHost() {
    return Optional.ofNullable(System.getProperty("EZY_CHAT_SERVER_HOST"))
        .orElse(Optional.ofNullable(System.getenv("EZY_CHAT_SERVER_HOST"))
            .orElse("localhost")).trim();
  }

  public int getServerPort() {
    return Optional.ofNullable(System.getenv("EZY_CHAT_SERVER_PORT"))
        .or(() -> Optional.ofNullable(System.getProperty("EZY_CHAT_SERVER_PORT")))
        .map(Integer::parseInt)
        .orElse(3333);
  }
}
