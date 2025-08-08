package com.vtaveira.domain.usecases.dto;

import com.vtaveira.domain.gateways.MessengerData;
import com.vtaveira.domain.model.UserStatus;

public record StatusUpdate(
    String username,
    UserStatus status
) implements MessengerData {}
