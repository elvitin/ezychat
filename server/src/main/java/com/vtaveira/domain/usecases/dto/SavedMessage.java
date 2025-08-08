package com.vtaveira.domain.usecases.dto;

import com.vtaveira.domain.gateways.MessengerData;

import java.util.Date;

public record SavedMessage(long id, String fromUsername, String fullName, String content,
                           Date createdAt) implements MessengerData {}