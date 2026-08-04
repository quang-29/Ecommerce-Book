package org.example.bookstore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookstore.service.r2cloudfare.R2CloudFareService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final R2CloudFareService r2Service;

    public String renUniqueFilename(String subBucket, String filename) {
        if (filename == null) {
            return subBucket + "/" + UUID.randomUUID();
        }
        return subBucket + "/" + UUID.randomUUID() + filename.replaceAll("[^a-zA-Z0-9.]", "");
    }
    public Optional<String> uploadFile(String subBucket,
                                       MultipartFile file) {
        if (file == null) {
            log.info("Cannot upload file because file is null");
            return Optional.empty();
        }
        String fullKey = renUniqueFilename(subBucket, file.getOriginalFilename());
        try {
            String url = r2Service.uploadToPublicBucket(file.getBytes(), fullKey, file.getContentType());
            return Optional.of(url);
        } catch (Exception e) {
            log.error("Exception when upload file", e);
            return Optional.empty();
        }
    }
}
