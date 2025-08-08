package com.vtaveira.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Password {
  private String value;

  public boolean isEqualTo(String other) {
    return this.value.equals(other);
  }
}
