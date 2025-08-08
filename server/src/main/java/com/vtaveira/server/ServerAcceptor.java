package com.vtaveira.server;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;


@Slf4j
public class ServerAcceptor {
  private final int port;
  private final ConnectionsManager connectionsManager = ConnectionsManager.getInstance();

  public ServerAcceptor(int port) {
    this.port = port;
  }

  public void start() {
    try (var serverSocket = new ServerSocket(port)) {
      log.info("server started on port {}", port);
      while (!serverSocket.isClosed()) {
        var clientSocket = serverSocket.accept();
        var handler = new ProtobufConnectionHandler(clientSocket);
        var ipPort = Utils.getClientIPPort(clientSocket);
        this.connectionsManager.register(ipPort, handler);
        handler.events().onSetupStreamsError = _ -> this.connectionsManager.unregister(ipPort);
        handler.events().onReadDataError = _ -> this.connectionsManager.unregister(ipPort);
        handler.events().onWriteDataError = _ -> this.connectionsManager.unregister(ipPort);
        handler.start();
      }
    } catch (IOException e) {
      log.error("error starting server on port {}: {}", port, e.getMessage(), e);
      throw new IllegalStateException("failed to start server on port " + port, e);
    }
  }
}
