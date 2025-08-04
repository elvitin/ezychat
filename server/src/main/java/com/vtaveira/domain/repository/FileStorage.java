package com.vtaveira.domain.repository;

import com.vtaveira.domain.repository.dto.StoredFile;

import java.util.Optional;

public interface FileStorage {
  void save(String fileId, String contentType, byte[] fileContent);

  Optional<StoredFile> load(String fileId);
}
