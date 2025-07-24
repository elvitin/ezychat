package com.vtaveira.infra.config;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.vtaveira.infra.persistence.entity.GroupEntity;
import com.vtaveira.infra.persistence.entity.MessageEntity;
import com.vtaveira.infra.persistence.entity.UserEntity;
import com.vtaveira.infra.persistence.entity.UserGroupEntity;
import lombok.Getter;

import java.sql.SQLException;

public class DatabaseConfig {
  private static final String DATABASE_URL = "jdbc:sqlite:ezychat.db";

  @Getter
  private static ConnectionSource connectionSource;

  private DatabaseConfig() {
    throw new IllegalStateException("Utility class");
  }

  public static void initializeDatabase() throws SQLException {
    connectionSource = new JdbcConnectionSource(DATABASE_URL);
    createTables();
  }


  private static void createTables() throws SQLException {
    TableUtils.createTableIfNotExists(connectionSource, UserEntity.class);
    TableUtils.createTableIfNotExists(connectionSource, MessageEntity.class);
    TableUtils.createTableIfNotExists(connectionSource, GroupEntity.class);
    TableUtils.createTableIfNotExists(connectionSource, UserGroupEntity.class);
  }
}
