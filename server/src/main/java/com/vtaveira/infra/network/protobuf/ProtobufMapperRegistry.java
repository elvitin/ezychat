package com.vtaveira.infra.network.protobuf;

import com.vtaveira.builds.java.WrapperMessage;
import com.vtaveira.domain.gateways.MessengerData;
import com.vtaveira.domain.usecases.dto.*;
import com.vtaveira.infra.network.protobuf.mapper.*;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

import java.util.Optional;
import java.util.function.Supplier;


@UtilityClass
public class ProtobufMapperRegistry {
  private final Map<Class<? extends MessengerData>, Supplier<ProtobufMapper<?>>> domainToMapper = new HashMap<>();
  private final Map<WrapperMessage.PayloadCase, Supplier<ProtobufMapper<?>>> protoToMapper = new HashMap<>();

  static {
    domainToMapper.put(LoginResult.class, LoginResultMapper::new);
    domainToMapper.put(UserToAuth.class, UserToAuthMapper::new);
    domainToMapper.put(StatusUpdate.class, StatusUpdateMapper::new);
    domainToMapper.put(GroupedMessages.class, GroupedMessagesMapper::new);
    domainToMapper.put(RegisterResult.class, RegisterResultMapper::new);
    domainToMapper.put(SavedMessage.class, SavedMessageMapper::new);
    domainToMapper.put(PrivateMessageConfirmation.class, PrivateMessageConfirmationMapper::new);


    protoToMapper.put(WrapperMessage.PayloadCase.LOGINREQUEST, UserToAuthMapper::new);
    protoToMapper.put(WrapperMessage.PayloadCase.REGISTERREQUEST, RegisterUserMapper::new);
    protoToMapper.put(WrapperMessage.PayloadCase.PRIVATEMESSAGEREQUEST, PrivateMessageMapper::new);
  }

  public Optional<ProtobufMapper<?>> getFromDomain(MessengerData domainClass) {
    return Optional.ofNullable(domainToMapper.get(domainClass.getClass()))
        .map(Supplier::get);
  }

  public Optional<ProtobufMapper<?>> getFromPayload(WrapperMessage.PayloadCase payloadCase) {
    return Optional.ofNullable(protoToMapper.get(payloadCase))
        .map(Supplier::get);
  }
}