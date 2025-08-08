package com.vtaveira.domain.usecases.dto;

import com.vtaveira.domain.gateways.MessengerData;

public record RegisterResult(
    boolean success,
    RegisterStatus status
) implements MessengerData {}
