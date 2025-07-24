package com.vtaveira.infra.database;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class DatabaseConfig {
  private static final String DATABASE_URL = "jdbc:sqlite:ezy_chat_client.db";

  public static void initialize() {
    log.info("initializing database with URL: {}", DATABASE_URL);
  }
}
