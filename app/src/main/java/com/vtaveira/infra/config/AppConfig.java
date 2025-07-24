package com.vtaveira.infra.config;


import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class AppConfig {
  public void initialize() {
    log.info("application configuration initialized successfully");
  }

  public void shutdown() {
    log.info("application configuration shutdown successfully");
  }
}
