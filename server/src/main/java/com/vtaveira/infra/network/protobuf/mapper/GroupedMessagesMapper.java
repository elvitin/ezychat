package com.vtaveira.infra.network.protobuf.mapper;

import com.google.protobuf.ByteString;
import com.vtaveira.builds.java.*;
import com.vtaveira.domain.model.Message;
import com.vtaveira.domain.repository.FileStorage;
import com.vtaveira.domain.usecases.dto.GroupedMessages;
import com.vtaveira.domain.usecases.dto.UserKey;
import com.vtaveira.infra.network.protobuf.ProtobufMapper;
import com.vtaveira.infra.persistence.repository.AwsS3Storage;

import java.util.List;


public class GroupedMessagesMapper implements ProtobufMapper<GroupedMessages> {
  private final FileStorage storage = new AwsS3Storage();

  @Override
  public WrapperMessage toProto(GroupedMessages data) {
    var builder = LoginResponse.newBuilder()
        .setSuccess(true);

    data.groups()
        .entrySet()
        .stream()
        .map(entry -> this.buildConversation(entry.getKey(), entry.getValue()))
        .forEach(builder::addConversation);

    return WrapperMessage.newBuilder().setLoginResponse(builder.build()).build();
  }

  @Override
  public GroupedMessages fromProto(WrapperMessage message) {
    return null;
  }

  private Conversation buildConversation(UserKey userKey, List<Message> messages) {
    var builder = Conversation.newBuilder()
        .setToUsername(userKey.username())
        .setFriendFullName(userKey.fullName());

    this.buildMessages(builder, messages, userKey);
    this.setProfilePicture(builder, userKey);

    return builder.build();
  }

  private void buildMessages(Conversation.Builder builder, List<Message> messages, UserKey userKey) {
    messages.forEach(message -> {
      var messageBuilder = MessageRestored.newBuilder()
          .setId(message.getId())
          .setContent(message.getContent())
          .setCreatedAt(message.getCreatedAt().toInstant().toString())
          .setFromMe(message.getSender().getUsername().equals(userKey.username()));

      builder.addMessages(messageBuilder.build());
    });
  }

  private void setProfilePicture(Conversation.Builder builder, UserKey userKey) {
    storage.load(userKey.username()).ifPresent(file -> {
      var picture = File.newBuilder()
          .setData(ByteString.copyFrom(file.content()))
          .setContentType(file.contentType());

      builder.setFile(picture.build());
    });
  }
}
