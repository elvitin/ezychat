package com.vtaveira.infra.network.protobuf;

import com.vtaveira.builds.java.WrapperMessage;
import com.vtaveira.domain.gateways.MessengerData;

public interface ProtobufMapper<T extends MessengerData> {
  WrapperMessage toProto(T data);

  T fromProto(WrapperMessage message);
}
