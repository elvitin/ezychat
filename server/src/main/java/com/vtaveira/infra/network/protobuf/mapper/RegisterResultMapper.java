package com.vtaveira.infra.network.protobuf.mapper;

import com.vtaveira.builds.java.RegisterResponse;
import com.vtaveira.builds.java.WrapperMessage;
import com.vtaveira.domain.usecases.dto.RegisterResult;
import com.vtaveira.infra.network.protobuf.ProtobufMapper;

public class RegisterResultMapper implements ProtobufMapper<RegisterResult> {
  @Override
  public WrapperMessage toProto(RegisterResult data) {
    var builder = RegisterResponse.newBuilder()
        .setSuccess(data.success())
        .setMessage(data.status().name()).build();

    return WrapperMessage.newBuilder()
        .setRegisterResponse(builder)
        .build();
  }

  @Override
  public RegisterResult fromProto(WrapperMessage message) {
    return null;
  }
}
