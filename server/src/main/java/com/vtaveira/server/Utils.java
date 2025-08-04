package com.vtaveira.server;

import lombok.experimental.UtilityClass;

import java.net.Socket;

@UtilityClass
public class Utils {
  public static String getClientIPPort(Socket socket) {
    return socket.getInetAddress() + ":" + socket.getPort();
  }
}
