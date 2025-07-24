package com.vtaveira.server;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;


@Slf4j
public class ServerAcceptor {
  private final int port;
  private final ClientManager clientManager = new ClientManager();

  public ServerAcceptor(int port) {
    this.port = port;
  }

  public void start() {
    try (var serverSocket = new ServerSocket(port)) {
      log.info("server started on port {}", port);
      while (!serverSocket.isClosed()) {
        var clientSocket = serverSocket.accept();
        new ClientHandler(clientSocket, clientManager);
      }

    } catch (IOException | SQLException e) {
      log.error("error starting server on port {}: {}", port, e.getMessage(), e);
      throw new IllegalStateException("failed to start server on port " + port, e);
    }
  }
}
