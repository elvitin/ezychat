package com.vtaveira.domain.usecases.dto;

import com.vtaveira.domain.gateways.MessengerData;

public record LoginResult(
    boolean success,
    LoginStatus status
) implements MessengerData {}
