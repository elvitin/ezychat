package com.vtaveira.server;

import java.net.Socket;

public class Utils {
  private Utils() {
    throw new IllegalStateException("Utility class");
  }
  public static String getClientIPPort(Socket socket) {
    return socket.getInetAddress() + ":" + socket.getPort();
  }
}
