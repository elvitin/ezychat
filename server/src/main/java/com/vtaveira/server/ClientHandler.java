package com.vtaveira.server;

import com.google.protobuf.ByteString;
import com.vtaveira.builds.java.*;
import com.vtaveira.domain.model.User;
import com.vtaveira.domain.service.*;
import com.vtaveira.infra.persistence.repository.AwsS3Storage;
import com.vtaveira.main.factories.MessageServiceFactory;
import com.vtaveira.main.factories.UserServiceFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

@Slf4j
public class ClientHandler implements Runnable {
  private final Socket socket;
  private final ClientManager clientManager;
  private final AwsS3Storage fileStorage;

  private final DataInputStream inFromClient; // to free
  private final DataOutputStream outToClient; // to free


  //Services
  private final UserService userService;
  private final MessageService messageService;

  //User
  private final UserSession userSession = new UserSession();


  public ClientHandler(Socket socket, ClientManager clientManager) throws SQLException, IOException {
    this.fileStorage = new AwsS3Storage();
    this.socket = socket;
    this.inFromClient = new DataInputStream(socket.getInputStream());
    this.outToClient = new DataOutputStream(socket.getOutputStream());
    this.clientManager = clientManager;

    this.userService = UserServiceFactory.getInstance();
    this.messageService = MessageServiceFactory.getInstance();

    this.userSession.setIpAddress(Utils.getClientIPPort(socket));

    var thread = new Thread(this);
    thread.setDaemon(true);

    thread.setName(this.userSession.getIpAddress());
    thread.start();

    log.info("client connected: {}", this.userSession.getIpAddress());
  }

  @Override
  public void run() {
    try {
      while (this.socket.isConnected()) {

        log.debug("waiting for size of payload from client: [{}]", this.userSession.getIpAddress());
        var len = this.inFromClient.readInt();
        log.debug("reading {} bytes from client: [{}]", len, this.userSession.getIpAddress());
        var payload = WrapperMessage.parseFrom(inFromClient.readNBytes(len));

        log.debug("message received from client [{}], type [{}]", this.userSession.getIpAddress(), payload.getPayloadCase().name());

        switch (payload.getPayloadCase()) {
          case LOGINREQUEST -> this.handleLogin(payload.getLoginRequest());
          case REGISTERREQUEST -> this.handleRegister(payload.getRegisterRequest());
          case PRIVATEMESSAGEREQUEST -> this.handlePrivateMessage(payload.getPrivateMessageRequest());
          case PAYLOAD_NOT_SET -> log.warn("payload not set");
          default -> log.warn("payload not recognized");
        }
      }
    } catch (IOException e) {
      this.clientManager.unregisterClient(this.userSession.getUsername());

      if (e.getLocalizedMessage().equals("Connection reset")) {
        log.warn("client disconnected: {}", this.userSession.getIpAddress());
        return;
      }

      log.error("unknown error, socket state {}: {}", this.socket.isConnected() ? "Connected" : "Disconnected", e.getLocalizedMessage());
    }
  }

  private void handlePrivateMessage(PrivateMessageRequest privateMessage) {
    var toUsername = privateMessage.getToUsername();
    var content = privateMessage.getContent();
    this.messageService.save(this.userSession.getUsername(), toUsername, content, SendPrivateMessageActions.builder()
        .onMessageSaved(savedMessage -> {

          var response = PrivateMessageResponse.newBuilder()
              .setSuccess(true)
              .setId(savedMessage.id())
              .build();


          var payload = WrapperMessage.newBuilder()
              .setPrivateMessageResponse(response)
              .build();

          this.sendMessage(payload);

          var message = ChatMessage.newBuilder()
              .setId(savedMessage.id())
              .setUsername(this.userSession.getUsername())
              .setFullName(savedMessage.fullName())
              .setContent(savedMessage.content())
              .setCreatedAt(savedMessage.createdAt().toInstant().toString());

          var messagePayload = WrapperMessage.newBuilder()
              .setChatMessage(message)
              .build();

          this.clientManager.getClient(toUsername).ifPresent(handler -> handler.sendMessage(messagePayload));
        }).build()
    );
  }

  private void handleLogin(LoginRequest loginRequest) {
    var username = loginRequest.getUsername();
    var password = loginRequest.getPassword();

    var response = LoginResponse.newBuilder();

    var actions = LoginActions.builder()
        .onSuccess(user -> {
          var statusPayload = UserStatusUpdate.newBuilder()
              .setUsername(user.getUsername())
              .setStatus(user.getStatus().toString())
              .build();

          this.userSession.setUsername(user.getUsername());
          this.userSession.setStatus(user.getStatus());

          this.clientManager.registerClient(user.getUsername(), this);

          response.setSuccess(true)
              .setStatus(user.getStatus().toString());


          this.messageService.getPendingMessagesGroupedByConversation(user.getUsername()).entrySet()
              .stream()
              .map(entry -> {
                    var conversation = Conversation.newBuilder()
                        .setToUsername(entry.getKey().username())
                        .setFriendFullName(entry.getKey().fullName())
                        .addAllMessages(
                            entry.getValue().stream()
                                .map(message -> MessageRestored.newBuilder()
                                    .setId(message.getId())
                                    .setContent(message.getContent())
                                    .setCreatedAt(message.getCreatedAt().toInstant().toString())
                                    .setFromMe(message.getSender().getUsername().equals(user.getUsername()))
                                    .build()
                                ).toList()
                        );
                    this.fileStorage.load(entry.getKey().username()).ifPresent(file ->
                        conversation.setFile(
                            File.newBuilder()
                                .setData(ByteString.copyFrom(file.content()))
                                .setContentType(file.contentType())
                                .build()
                        )
                    );
                    return conversation;
                  }
              )
              .forEach(response::addConversation);

          var notification = WrapperMessage.newBuilder()
              .setUserStatusUpdate(statusPayload)
              .build();

          //this.clientManager.broadcastMessage(notification, this.userSession.getUsername());
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
          var registerResponse = RegisterResponse.newBuilder()
              .setSuccess(true)
              .setMessage("Usuário registrado com sucesso")
              .build();

          if (registerRequest.hasProfilePicture()) {
            var profilePicture = registerRequest.getProfilePicture();
            var data = profilePicture.getData().toByteArray();
            this.fileStorage.save(user.getUsername(), profilePicture.getContentType(), data);
          }
          response.setRegisterResponse(registerResponse);
        })
        .onUsernameAlreadyInUse(() -> {
          log.warn("username already in use: {}", registerRequest.getUsername());
          var registerResponse = RegisterResponse.newBuilder()
              .setSuccess(false)
              .setMessage("Nome de usuário já está em uso")
              .build();
          response.setRegisterResponse(registerResponse);
        })
        .onEmailAlreadyInUse(() -> {
          log.warn("email already in use: {}", registerRequest.getEmail());
          var registerResponse = RegisterResponse.newBuilder()
              .setSuccess(false)
              .setMessage("Email já está em uso")
              .build();

          response.setRegisterResponse(registerResponse);
        })
    );
    this.sendMessage(response.build());
  }

  public void sendMessage(WrapperMessage payload) {
    try {
      var bytes = payload.toByteArray();
      this.outToClient.writeInt(bytes.length);
      this.outToClient.write(bytes);
      log.debug("message sent to client ip [{}] size [{}]", this.userSession.getIpAddress(), bytes.length);
    } catch (IOException e) {
      log.error("error sending message to client ip [{}]: {}", this.userSession.getIpAddress(), e.getLocalizedMessage());
    }
  }
}
