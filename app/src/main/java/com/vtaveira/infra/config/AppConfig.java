package com.vtaveira.infra.config;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(fluent = true)
@UtilityClass
public class AppConfig {

  @Getter
  private final double initialWidth = 500.0;

  @Getter
  private final double initialHeight = 500.0;

  public void initialize() {
    log.info("application configuration initialized successfully");
  }

  public void shutdown() {
    log.info("application configuration shutdown successfully");
  }
}
