package com.vtaveira.infra.network.protobuf.mapper;

import com.vtaveira.builds.java.WrapperMessage;
import com.vtaveira.domain.usecases.dto.PrivateMessageToSent;
import com.vtaveira.infra.network.protobuf.ProtobufMapper;

public class PrivateMessageMapper implements ProtobufMapper<PrivateMessageToSent> {
  @Override
  public WrapperMessage toProto(PrivateMessageToSent data) {
    return null;
  }

  @Override
  public PrivateMessageToSent fromProto(WrapperMessage message) {
    var proto = message.getPrivateMessageRequest();
    return new PrivateMessageToSent(proto.getToUsername(), proto.getContent());
  }
}
