package com.vtaveira.domain.service.dto;

import java.util.Date;

public record SavedMessage(long id, String fullName, String content, Date createdAt) {
}
