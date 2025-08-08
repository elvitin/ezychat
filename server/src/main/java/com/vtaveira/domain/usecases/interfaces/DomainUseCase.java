package com.vtaveira.domain.usecases.interfaces;

import com.vtaveira.domain.gateways.MessengerData;

public interface DomainUseCase<T extends MessengerData> {
  void execute(T data);
}

