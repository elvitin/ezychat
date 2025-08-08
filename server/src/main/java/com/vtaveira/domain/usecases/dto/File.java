package com.vtaveira.domain.usecases.dto;

public record File(
    byte[] content,
    String type
) {}
