package com.vtaveira.server;

import com.vtaveira.builds.java.WrapperMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ClientManager {
  private final ConcurrentHashMap<String, ClientHandler> clients = new ConcurrentHashMap<>();


  public void registerClient(String username, ClientHandler handler) {
    this.clients.put(username, handler);
  }

  public Optional<ClientHandler> getClient(String username) {
    return this.clients.containsKey(username) ?
        Optional.of(this.clients.get(username)) :
        Optional.empty();
  }

  public void unregisterClient(String username) {
    Optional.ofNullable(username).ifPresent(this.clients::remove);
  }

  public void broadcastMessage(WrapperMessage message, String excludeUsername) {
    for (var entry : this.clients.entrySet()) {
      if (!entry.getKey().equals(excludeUsername)) {
        entry.getValue().sendMessage(message);
      }
    }
  }
}
