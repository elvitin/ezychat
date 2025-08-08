package com.vtaveira.domain.usecases.dto;

import com.vtaveira.domain.gateways.MessengerData;

public record PrivateMessageConfirmation(boolean success, long id) implements MessengerData {}
