package com.vtaveira.infra.network.protobuf.mapper;

import com.vtaveira.builds.java.PrivateMessageResponse;
import com.vtaveira.builds.java.WrapperMessage;
import com.vtaveira.domain.usecases.dto.PrivateMessageConfirmation;
import com.vtaveira.infra.network.protobuf.ProtobufMapper;

public class PrivateMessageConfirmationMapper implements ProtobufMapper<PrivateMessageConfirmation> {
  @Override
  public WrapperMessage toProto(PrivateMessageConfirmation data) {
    var builder = PrivateMessageResponse.newBuilder()
        .setSuccess(true)
        .setId(data.id());

    return WrapperMessage.newBuilder().setPrivateMessageResponse(builder.build()).build();
  }

  @Override
  public PrivateMessageConfirmation fromProto(WrapperMessage message) {
    return null;
  }
}
