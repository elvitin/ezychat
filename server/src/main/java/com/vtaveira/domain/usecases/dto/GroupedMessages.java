package com.vtaveira.domain.usecases.dto;

import com.vtaveira.domain.gateways.MessengerData;
import com.vtaveira.domain.model.Message;

import java.util.List;
import java.util.Map;

public record GroupedMessages(
    Map<UserKey, List<Message>> groups
) implements MessengerData {}
