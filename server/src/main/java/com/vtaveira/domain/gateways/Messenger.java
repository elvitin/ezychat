package com.vtaveira.domain.gateways;


import java.util.Optional;

public interface Messenger {

  void markAsAvailable(String uniqueIdentity);

  Optional<String> getAvailableIdentity();

  void sent(MessengerData message);

  void sentTo(MessengerData message, String uniqueIdentity);

  void sentTo(MessengerData message, String... uniqueIdentities);
}
