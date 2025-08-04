package com.vtaveira.infra.config;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.vtaveira.infra.persistence.entity.GroupEntity;
import com.vtaveira.infra.persistence.entity.MessageEntity;
import com.vtaveira.infra.persistence.entity.UserEntity;
import com.vtaveira.infra.persistence.entity.UserGroupEntity;
import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.sql.SQLException;

@UtilityClass
public class DatabaseConfig {
  private final String DATABASE_URL = "jdbc:sqlite:ezychat.db";

  @Getter
  private ConnectionSource connectionSource;

  public void initializeDatabase() throws SQLException {
    connectionSource = new JdbcConnectionSource(DATABASE_URL);
    createTables();
  }

  private void createTables() throws SQLException {
    TableUtils.createTableIfNotExists(connectionSource, UserEntity.class);
    TableUtils.createTableIfNotExists(connectionSource, MessageEntity.class);
    TableUtils.createTableIfNotExists(connectionSource, GroupEntity.class);
    TableUtils.createTableIfNotExists(connectionSource, UserGroupEntity.class);
  }
}
