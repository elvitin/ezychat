package com.vtaveira.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationUtil {

  public static boolean isValidEmail(String email) {
    if (email == null || email.trim().isEmpty()) {
      return false;
    }
    String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
    return email.matches(emailRegex);
  }
}
