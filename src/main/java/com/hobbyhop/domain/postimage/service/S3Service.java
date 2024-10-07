package com.hobbyhop.domain.postimage.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.hobbyhop.domain.postimage.dto.PostImageDTO;
import com.hobbyhop.global.exception.s3.ImageSaveException;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public PostImageDTO saveFile(MultipartFile multipartFile) {
        String filename = multipartFile.getOriginalFilename();
        String savedFilename = UUID.randomUUID() + "_" + filename;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        try {
            amazonS3.putObject(bucket, savedFilename, multipartFile.getInputStream(), metadata);
        } catch (Exception e) {
            throw new ImageSaveException();
        }

        String savedFileUrl = amazonS3.getUrl(bucket, savedFilename).toString();

        return new PostImageDTO(filename, savedFilename, savedFileUrl);
    }

    public void deleteImage(String savedFileName) {
        amazonS3.deleteObject(bucket, savedFileName);
    }
}