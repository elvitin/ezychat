package com.vtaveira.infra.network.protobuf.mapper;

import com.vtaveira.builds.java.ChatMessage;
import com.vtaveira.builds.java.WrapperMessage;
import com.vtaveira.domain.usecases.dto.SavedMessage;
import com.vtaveira.infra.network.protobuf.ProtobufMapper;

public class SavedMessageMapper implements ProtobufMapper<SavedMessage> {
  @Override
  public WrapperMessage toProto(SavedMessage data) {
    var builder = ChatMessage.newBuilder()
        .setId(data.id())
        .setUsername(data.fromUsername())
        .setFullName(data.fullName())
        .setContent(data.content())
        .setCreatedAt(data.createdAt().toInstant().toString()).build();

    return WrapperMessage.newBuilder().setChatMessage(builder).build();
  }

  @Override
  public SavedMessage fromProto(WrapperMessage message) {
    return null;
  }
}
