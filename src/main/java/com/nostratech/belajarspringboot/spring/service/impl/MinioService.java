// package com.nostratech.belajarspringboot.spring.service.impl;

// import com.nostratech.belajarspringboot.spring.dto.PresignUrlResponseDTO;
// import io.minio.*;
// import io.minio.http.Method;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;

// import java.io.InputStream;
// import java.util.UUID;

// @Service
// public class MinioService {

//     private final MinioClient minioClient;
//     private final String bucketName;

//     public MinioService(MinioClient minioClient, @Value("${minio.bucket}") String bucketName) {
//         this.minioClient = minioClient;
//         this.bucketName = bucketName;
//     }

//     public void initializeBucket() {
//         try {
//             boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
//                     .bucket(bucketName)
//                     .build());
            
//             if (!bucketExists) {
//                 minioClient.makeBucket(MakeBucketArgs.builder()
//                         .bucket(bucketName)
//                         .build());
//             }
//         } catch (Exception e) {
//             throw new RuntimeException("Error initializing MinIO bucket", e);
//         }
//     }

//     public String uploadFile(MultipartFile file) {
//         try {
//             String fileName = generateFileName(file.getOriginalFilename());
            
//             minioClient.putObject(PutObjectArgs.builder()
//                     .bucket(bucketName)
//                     .object(fileName)
//                     .stream(file.getInputStream(), file.getSize(), -1)
//                     .contentType(file.getContentType())
//                     .build());

//             return fileName;
//         } catch (Exception e) {
//             throw new RuntimeException("Error uploading file to MinIO", e);
//         }
//     }

//     public InputStream downloadFile(String fileName) {
//         try {
//             return minioClient.getObject(GetObjectArgs.builder()
//                     .bucket(bucketName)
//                     .object(fileName)
//                     .build());
//         } catch (Exception e) {
//             throw new RuntimeException("Error downloading file from MinIO", e);
//         }
//     }

//     public void deleteFile(String fileName) {
//         try {
//             minioClient.removeObject(RemoveObjectArgs.builder()
//                     .bucket(bucketName)
//                     .object(fileName)
//                     .build());
//         } catch (Exception e) {
//             throw new RuntimeException("Error deleting file from MinIO", e);
//         }
//     }

//     private String generateFileName(String originalFileName) {
//         String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
//         return UUID.randomUUID().toString() + extension;
//     }

//     public PresignUrlResponseDTO generatePresignedUrl(String fileName) {
//         try {
//             String url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
//                     .bucket(bucketName)
//                     .object(fileName)
//                     .method(Method.GET)
//                     .expiry(60 * 60 * 24)
//                     .build());
            
//             return new PresignUrlResponseDTO(url);
//         } catch (Exception e) {
//             throw new RuntimeException("Error generating presigned URL", e);
//         }
//     }
// } 