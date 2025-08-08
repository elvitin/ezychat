package com.vtaveira.infra.network.protobuf.mapper;

import com.vtaveira.builds.java.LoginRequest;
import com.vtaveira.builds.java.WrapperMessage;
import com.vtaveira.domain.usecases.dto.UserToAuth;
import com.vtaveira.infra.network.protobuf.ProtobufMapper;

public class UserToAuthMapper implements ProtobufMapper<UserToAuth> {

  @Override
  public WrapperMessage toProto(UserToAuth user) {
    var builder = LoginRequest.newBuilder()
        .setUsername(user.username())
        .setPassword(user.password());

    return WrapperMessage.newBuilder().setLoginRequest(builder.build()).build();
  }

  @Override
  public UserToAuth fromProto(WrapperMessage message) {
    var loginRequest = message.getLoginRequest();
    return new UserToAuth(loginRequest.getUsername(), loginRequest.getPassword());
  }
}
