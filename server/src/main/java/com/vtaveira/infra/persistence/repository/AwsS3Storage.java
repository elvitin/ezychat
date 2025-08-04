package com.vtaveira.infra.persistence.repository;

import com.vtaveira.domain.repository.FileStorage;
import com.vtaveira.domain.repository.dto.StoredFile;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AnonymousCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;


import java.net.URI;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class AwsS3Storage implements FileStorage {

  private static final String BUCKET_NAME = "ezychat-bucket";
  private final S3Client s3Client;

  public AwsS3Storage() {
    this.s3Client = S3Client.builder()
        .endpointOverride(URI.create("http://127.0.0.1:4566"))
        .credentialsProvider(AnonymousCredentialsProvider.create())
        .region(Region.US_EAST_1)
        .build();

    this.initializeBucket();
  }


  private void initializeBucket() {
    try {
      s3Client.headBucket(req -> req.bucket(BUCKET_NAME).build());
    } catch (NoSuchBucketException _) {
      s3Client.createBucket(req -> req.bucket(BUCKET_NAME).build());
    }
  }

  @Override
  public void save(String fileId, String contentType, byte[] fileContent) {
    this.s3Client.putObject(req -> req
        .bucket(BUCKET_NAME)
        .key(fileId)
        .metadata(Map.of("ext", contentType.split("/")[1]))
        .contentType(contentType)
        .build(), RequestBody.fromBytes(fileContent));
  }

  @Override
  public Optional<StoredFile> load(String fileId) {
    try {
      var result = this.s3Client
          .getObjectAsBytes(req -> req.bucket(BUCKET_NAME).key(fileId).build());
      var response = result.response();
      var contentType = response.contentType();
      var map = response.metadata();
      byte[] fileContent = result.asByteArray();
      return Optional.of(new StoredFile(fileContent, contentType, map.get("ext")));
    } catch (NoSuchKeyException _) {
      return Optional.empty();
    }
  }
}
