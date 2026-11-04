package org.example.bookstore.service.r2cloudfare;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URI;

@Slf4j
@Service
public class R2CloudFareService {
    @Value("${r2.access-key}")
    private String accessKey;

    @Value("${r2.secret-key}")
    private String secretKey;

    @Value("${r2.bucket}")
    private String bucketPublic;

    @Value("${r2.public-url}")
    private String urlPublicPrefix;

    @Value("${r2.endpoint}")
    private String endpoint;

    private S3Client s3Client;

    @PostConstruct
    public void init() {
        StaticCredentialsProvider credentials = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey));

        this.s3Client = S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(credentials)
                .region(Region.of("auto")) // R2 dùng "auto"
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .build();
    }

    @PreDestroy
    private void destroy() {
        if (s3Client != null) {
            s3Client.close();
        }
    }

    public String uploadToPublicBucket(byte[] contents, String key, String contentType) {
        log.info("[R2] Uploading file, key: {}, size: {} bytes, contentType: {}", key, contents.length, contentType);

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketPublic)
                        .key(key)
                        .contentType(contentType)
                        .contentLength((long) contents.length)
                        .acl(ObjectCannedACL.PUBLIC_READ)
                        .build(),
                RequestBody.fromBytes(contents));

        log.info("[R2] Upload success, key: {}", key);
        return urlPublicPrefix + key;
    }

}
