package com.vtaveira.domain.usecases.dto;

import com.vtaveira.domain.gateways.MessengerData;

public record PrivateMessageToSent(
    String toUsername,
    String content
) implements MessengerData {}
