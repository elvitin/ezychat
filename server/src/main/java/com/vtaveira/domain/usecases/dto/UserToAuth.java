package com.vtaveira.domain.usecases.dto;

import com.vtaveira.domain.gateways.MessengerData;

public record UserToAuth(
    String username,
    String password
) implements MessengerData {}
