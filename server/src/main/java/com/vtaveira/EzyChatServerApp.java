package com.vtaveira;

import com.vtaveira.infra.config.DatabaseConfig;
import com.vtaveira.server.ServerAcceptor;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;


@Slf4j
public class EzyChatServerApp {

  public static void main(String[] args) throws SQLException {
    DatabaseConfig.initializeDatabase();
    new ServerAcceptor(3333).start();
  }
}