package com.vtaveira.infra.network.protobuf.mapper;

import com.vtaveira.builds.java.LoginResponse;
import com.vtaveira.builds.java.WrapperMessage;
import com.vtaveira.domain.usecases.dto.LoginResult;
import com.vtaveira.infra.network.protobuf.ProtobufMapper;

public class LoginResultMapper implements ProtobufMapper<LoginResult> {
  @Override
  public WrapperMessage toProto(LoginResult data) {
    var builder = LoginResponse.newBuilder()
        .setSuccess(data.success())
        .setStatus(data.status().name());

    return WrapperMessage.newBuilder()
        .setLoginResponse(builder.build())
        .build();
  }

  @Override
  public LoginResult fromProto(WrapperMessage message) {
    return null;
  }
}
