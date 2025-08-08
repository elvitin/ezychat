package com.vtaveira.infra.network.protobuf.mapper;

import com.vtaveira.builds.java.WrapperMessage;
import com.vtaveira.domain.usecases.dto.File;
import com.vtaveira.domain.usecases.dto.UserToRegister;
import com.vtaveira.infra.network.protobuf.ProtobufMapper;


import java.util.Optional;

public class RegisterUserMapper implements ProtobufMapper<UserToRegister> {

  @Override
  public WrapperMessage toProto(UserToRegister data) {
    return null;
  }

  @Override
  public UserToRegister fromProto(WrapperMessage message) {
    var registerRequest = message.getRegisterRequest();
    return new UserToRegister(
        registerRequest.getUsername(),
        registerRequest.getFullName(),
        registerRequest.getEmail(),
        registerRequest.getPassword(),
        registerRequest.hasProfilePicture() ?
            Optional.of(new File(
                registerRequest.getProfilePicture().getData().toByteArray(),
                registerRequest.getProfilePicture().getContentType()
            )) : Optional.empty()
    );
  }
}
