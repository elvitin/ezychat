package com.vtaveira.infra.network.protobuf.mapper;

import com.vtaveira.builds.java.UserStatusUpdate;
import com.vtaveira.builds.java.WrapperMessage;
import com.vtaveira.domain.model.UserStatus;
import com.vtaveira.domain.usecases.dto.StatusUpdate;
import com.vtaveira.infra.network.protobuf.ProtobufMapper;


public class StatusUpdateMapper implements ProtobufMapper<StatusUpdate> {
  @Override
  public WrapperMessage toProto(StatusUpdate status) {
    var statusPayload = UserStatusUpdate.newBuilder()
        .setUsername(status.username())
        .setStatus(status.status().name())
        .build();

    return WrapperMessage.newBuilder()
        .setUserStatusUpdate(statusPayload)
        .build();
  }

  @Override
  public StatusUpdate fromProto(WrapperMessage message) {
    var statusUpdate = message.getUserStatusUpdate();
    return new StatusUpdate(
        statusUpdate.getUsername(),
        UserStatus.valueOf(statusUpdate.getStatus())
    );
  }
}
