package com.vtaveira.client.service;

import com.vtaveira.builds.java.LoginRequest;
import com.vtaveira.builds.java.RegisterRequest;
import com.vtaveira.builds.java.WrapperMessage;
import com.vtaveira.client.network.NetworkClient;
import com.vtaveira.infra.config.NetworkConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class AuthService {

  private final NetworkClient networkClient;

  public AuthService() {
    this.networkClient = new NetworkClient();
  }

  public CompletableFuture<Boolean> login(String username, String password) {
    return CompletableFuture.supplyAsync(() -> {
      if (this.networkClient.connect(NetworkConfig.getServerHost(), NetworkConfig.getServerPort())) {
        var loginRequest = LoginRequest.newBuilder()
            .setUsername(username)
            .setPassword(password)
            .build();

        var wrapperMessage = WrapperMessage.newBuilder()
            .setLoginRequest(loginRequest)
            .build();

        this.networkClient.sendMessage(wrapperMessage);

        return true;
      }
      return false;
    });
  }

  public CompletableFuture<Boolean> register(String username, String fullName, String email, String password) {
    log.debug("registering user: {}", username);
    return CompletableFuture.supplyAsync(() -> {
      if (!this.networkClient.isConnected()) {
        log.warn("not connected [{}], trying to reconnect...", this.networkClient.isConnected());
        if (!this.networkClient.connect(NetworkConfig.getServerHost(), NetworkConfig.getServerPort())) {
          return false;
        }
      }

      var registerRequest = RegisterRequest.newBuilder()
          .setUsername(username)
          .setFullName(fullName)
          .setEmail(email)
          .setPassword(password)
          .build();

      var wrapperMessage = WrapperMessage.newBuilder()
          .setRegisterRequest(registerRequest)
          .build();

      log.debug("sending register request: {}", wrapperMessage);
      this.networkClient.sendMessage(wrapperMessage);
      return true;
    });
  }
}
