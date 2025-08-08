package com.vtaveira.server;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ConnectionsManager {

  private static ConnectionsManager instance;
  private final ConcurrentHashMap<String, ConnectionHandler> connections = new ConcurrentHashMap<>();

  public static synchronized ConnectionsManager getInstance() {
    return Optional.ofNullable(ConnectionsManager.instance)
        .orElseGet(() -> ConnectionsManager.instance = new ConnectionsManager());
  }

  public void register(String uniqueIdentity, ConnectionHandler handler) {
    this.connections.put(uniqueIdentity, handler);
  }

  public Optional<ConnectionHandler> get(String uniqueIdentity) {
    return this.connections.containsKey(uniqueIdentity)
        ? Optional.of(this.connections.get(uniqueIdentity))
        : Optional.empty();
  }


  public void changeIdentity(String oldIdentity, String newIdentity) {
    this.get(oldIdentity).ifPresent(handler -> {
      this.connections.remove(oldIdentity);
      //TODO: things can still go wrong, the try may have been executed before the put happens
      //TODO: The put should happen only when it is certain that no try has been executed in ConnectionHandler.
      //TODO: The solution might be to use a lock (block any operation that throws an exception in ConnectionHandler,
      //assign the events and then unlock, so when the exception occurs, the listeners will already be configured.
      this.connections.put(newIdentity, handler);
    });
  }

  public void unregister(String uniqueIdentity) {
    Optional.ofNullable(uniqueIdentity).ifPresent(this.connections::remove);
  }
}
