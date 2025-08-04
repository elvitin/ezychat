package com.vtaveira.domain.repository.dto;

public record StoredFile(byte[] content, String contentType, String ext) {
}
