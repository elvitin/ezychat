package com.vtaveira.server;


import com.vtaveira.builds.java.*;
import com.vtaveira.domain.model.User;
import com.vtaveira.domain.service.*;
import com.vtaveira.main.factories.MessageServiceFactory;
import com.vtaveira.main.factories.UserServiceFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;

@Slf4j
public class ClientHandler implements Runnable {
  private final Socket socket;
  private final ClientManager clientManager;


  private final InputStream inFromClient; // to free
  private final OutputStream outToClient; // to free


  //Services
  private final UserService userService;
  private final MessageService messageService;

  //User
  private final UserSession userSession = new UserSession();


  public ClientHandler(Socket socket, ClientManager clientManager) throws SQLException, IOException {
    this.socket = socket;
    this.inFromClient = socket.getInputStream();
    this.outToClient = socket.getOutputStream();
    this.clientManager = clientManager;

    this.userService = UserServiceFactory.getInstance();
    this.messageService = MessageServiceFactory.getInstance();

    this.userSession.setIpAddress(Utils.getClientIPPort(socket));

    Thread thread = new Thread(this);
    thread.setDaemon(true);

    thread.setName(this.userSession.getIpAddress());
    thread.start();

    this.clientManager.registerClient(this.userSession.getIpAddress(), this);

    log.info("client connected: {}", this.userSession.getIpAddress());
  }

  @Override
  public void run() {
    try {
      while (this.socket.isConnected()) {

        log.debug("waiting for size of payload from client: [{}]", this.userSession.getIpAddress());
        var len = this.inFromClient.read();
        log.debug("payload size: [{}]", len);
        log.debug("reading {} bytes from client: [{}]", len, this.userSession.getIpAddress());
        var payload = WrapperMessage.parseFrom(inFromClient.readNBytes(len));
        log.debug("message received from client [{}], type [{}]", this.userSession.getIpAddress(), payload.getPayloadCase().name());

        switch (payload.getPayloadCase()) {
          case LOGINREQUEST -> this.handleLogin(payload.getLoginRequest());
          case REGISTERREQUEST -> this.handleRegister(payload.getRegisterRequest());
          case PRIVATEMESSAGE -> this.handlePrivateMessage(payload.getPrivateMessage());
          case PAYLOAD_NOT_SET -> log.warn("payload not set");
          default -> log.warn("payload not recognized");
        }
      }
    } catch (IOException e) {
      // Transformar em ENUM o 'Connection reset'.
      if (e.getLocalizedMessage().equals("Connection reset")) {
        this.clientManager.unregisterClient(this.userSession.getIpAddress());
        log.warn("client disconnected: {}", this.userSession.getIpAddress());
        return;
      }
      log.error("unknown error, socket state {}: {}", this.socket.isConnected() ? "Connected" : "Disconnected", e.getLocalizedMessage());
    }
  }

  private void handlePrivateMessage(PrivateMessage privateMessage) {
    var receiverUsername = privateMessage.getRecipientUsername();
    var content = privateMessage.getMessageContent();
    this.messageService.sendPrivateMessage(this.userSession.getUsername(), receiverUsername, content, new SendPrivateMessageActions()
        .onMessageSaved(() -> {
          var ack = Ack.newBuilder()
              .setSucess(true)
              .setMessage("message sent successfully")
              .build();
          var payload = WrapperMessage.newBuilder().setAck(ack).build();

          this.clientManager.getClient(receiverUsername).sendMessage(payload);
        })
    );
  }

  private void handleLogin(LoginRequest loginRequest) {
    var username = loginRequest.getUsername();
    var password = loginRequest.getPassword();

    var response = LoginResponse.newBuilder();


    var actions = LoginActions.builder()
        .onSuccess(user -> {
          response.setSuccess(true)
              .setStatus(user.getStatus().toString())
              .addAllPendingMessages(messageService.getPendingMessages(user.getUsername()));

          var statusPayload = UserStatusUpdate.newBuilder()
              .setUsername(user.getUsername())
              .setStatus(user.getStatus().toString())
              .build();

          this.userSession.setUsername(user.getUsername());
          this.userSession.setStatus(user.getStatus());

          var notification = WrapperMessage.newBuilder()
              .setUserStatusUpdate(statusPayload)
              .build();

          this.clientManager.broadcastMessage(notification, this.userSession.getIpAddress());
        })
        .onUserNotFound(_ -> response.setSuccess(false).setErrorMessage("Usuário não encontrado"))
        .onInvalidPassword(_ -> response.setSuccess(false).setErrorMessage("Senha inválida"))
        .build();

    this.userService.login(username, password, actions);

    var payload = WrapperMessage.newBuilder()
        .setLoginResponse(response)
        .build();

    this.sendMessage(payload);
  }

  private void handleRegister(RegisterRequest registerRequest) {
    var user = User.builder()
        .username(registerRequest.getUsername())
        .fullName(registerRequest.getFullName())
        .email(registerRequest.getEmail())
        .password(registerRequest.getPassword())
        .build();

    log.debug("registering user: {}", user);
    var response = WrapperMessage.newBuilder();
    this.userService.register(user, new RegisterActions()
        .onUserRegistered(() -> {
          var ack = Ack.newBuilder()
              .setSucess(true)
              .setMessage("Usuário registrado com sucesso")
              .build();
          response.setAck(ack);
        })
        .onUsernameAlreadyInUse(() -> {
          log.warn("username already in use: {}", registerRequest.getUsername());
          var ack = Ack.newBuilder()
              .setSucess(false)
              .setMessage("Nome de usuário já está em uso")
              .build();
          response.setAck(ack);
        })
        .onEmailAlreadyInUse(() -> {
          log.warn("email already in use: {}", registerRequest.getEmail());
          var ack = Ack.newBuilder()
              .setSucess(false)
              .setMessage("Email já está em uso")
              .build();

          response.setAck(ack);
        })
    );
    this.sendMessage(response.build());
  }

  public void sendMessage(WrapperMessage payload) {
    try {
      log.debug("sending message to client ip [{}]", this.userSession.getIpAddress());
      var len = payload.getSerializedSize();
      this.outToClient.write(len);
      this.outToClient.write(payload.toByteArray());
      log.debug("message sent to client ip [{}]", this.userSession.getIpAddress());
    } catch (IOException e) {
      log.error("error sending message to client ip [{}]: {}", this.userSession.getIpAddress(), e.getLocalizedMessage());
    }
  }

  public String getIPAddress() {
    return this.userSession.getIpAddress();
  }
}
