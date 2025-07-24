package com.vtaveira.server;

import com.vtaveira.builds.java.WrapperMessage;

import java.util.concurrent.ConcurrentHashMap;

public class ClientManager {
  private final ConcurrentHashMap<String, ClientHandler> onlineClients = new ConcurrentHashMap<>();

  public void registerClient(String ip, ClientHandler handler) {
    onlineClients.put(ip, handler);
  }

  public void unregisterClient(String ip) {
    if (ip != null) onlineClients.remove(ip);
  }

  public ClientHandler getClient(String ip) {
    return onlineClients.get(ip);
  }

  public void broadcastMessage(WrapperMessage message, String excludeIp) {
    for (ClientHandler clientHandler : onlineClients.values()) {
      if (clientHandler.getIPAddress().equals(excludeIp)) continue;
      clientHandler.sendMessage(message);
    }
  }
}
