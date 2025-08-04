package com.vtaveira.domain.service;


import com.vtaveira.domain.service.dto.SavedMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;


import java.util.function.Consumer;


@Getter
@Accessors(fluent = true)
@Builder
public class SendPrivateMessageActions {

  Consumer<SavedMessage> onMessageSaved;
}
