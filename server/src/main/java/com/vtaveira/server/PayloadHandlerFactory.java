package com.vtaveira.server;

import com.vtaveira.builds.java.WrapperMessage;

import com.vtaveira.domain.gateways.Messenger;
import com.vtaveira.domain.usecases.Authorization;
import com.vtaveira.domain.usecases.RegisterUser;
import com.vtaveira.domain.usecases.interfaces.DomainUseCase;
import com.vtaveira.domain.usecases.SentPrivateMessage;
import com.vtaveira.infra.network.protobuf.ProtobufMessenger;
import com.vtaveira.infra.persistence.repository.AwsS3Storage;
import com.vtaveira.main.factories.MessageRepositoryFactory;
import com.vtaveira.main.factories.UserRepositoryFactory;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@NoArgsConstructor
public class PayloadHandlerFactory {


  @Setter private ConnectionHandler connectionHandler;

  private Supplier<Messenger> MessengerFactory = () -> new ProtobufMessenger(ConnectionsManager.getInstance(), this.connectionHandler);

  private Supplier<DomainUseCase> AuthorizationFactory = () -> {
    var userRepository = UserRepositoryFactory.getInstance();
    var messageRepository = MessageRepositoryFactory.getInstance();
    return new Authorization(userRepository, messageRepository, MessengerFactory.get());
  };

  private Supplier<DomainUseCase> RegisterFactory = () -> {
    var userRepository = UserRepositoryFactory.getInstance();
    return new RegisterUser(userRepository, MessengerFactory.get(), new AwsS3Storage());
  };

  private Supplier<DomainUseCase> SendPrivateMessageFactory = () -> {
    var userRepository = UserRepositoryFactory.getInstance();
    var messageRepository = MessageRepositoryFactory.getInstance();
    return new SentPrivateMessage(MessengerFactory.get(), userRepository, messageRepository);
  };

  private final Map<WrapperMessage.PayloadCase, Supplier<DomainUseCase>> handlers = Map.of(
      WrapperMessage.PayloadCase.LOGINREQUEST, AuthorizationFactory,
      WrapperMessage.PayloadCase.REGISTERREQUEST, RegisterFactory,
      WrapperMessage.PayloadCase.PRIVATEMESSAGEREQUEST, SendPrivateMessageFactory
  );

  public PayloadHandlerFactory(ConnectionHandler connectionHandler) {
    this.connectionHandler = connectionHandler;
  }

  public Optional<DomainUseCase> getHandler(WrapperMessage.PayloadCase payloadCase) {
    return Optional.ofNullable(handlers.get(payloadCase)).map(Supplier::get);
  }
}
