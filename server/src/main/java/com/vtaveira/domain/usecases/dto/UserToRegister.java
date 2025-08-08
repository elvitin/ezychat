package com.vtaveira.domain.usecases.dto;

import com.vtaveira.domain.gateways.MessengerData;

import java.util.Optional;

public record UserToRegister(
    String username,
    String fullName,
    String email,
    String password,
    Optional<File> profilePicture
) implements MessengerData {}