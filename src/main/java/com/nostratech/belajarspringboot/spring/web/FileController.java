// package com.nostratech.belajarspringboot.spring.web;
// import org.springframework.core.io.InputStreamResource;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;

// import com.nostratech.belajarspringboot.spring.service.impl.MinioService;
// import com.nostratech.belajarspringboot.spring.dto.PresignUrlResponseDTO;

// import java.io.InputStream;

// @RestController
// @RequestMapping("/api/files")
// public class FileController {

//     private final MinioService minioService;

//     public FileController(MinioService minioService) {
//         this.minioService = minioService;
//     }

//     @PostMapping("/upload")
//     public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
//         String fileName = minioService.uploadFile(file);
//         return ResponseEntity.ok(fileName);
//     }

//     @GetMapping("/download/{fileName}")
//     public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileName) {
//         InputStream fileStream = minioService.downloadFile(fileName);
//         InputStreamResource resource = new InputStreamResource(fileStream);

//         return ResponseEntity.ok()
//                 .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
//                 .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                 .body(resource);
//     }

//     @GetMapping("/presigned-url/{fileName}")
//     public ResponseEntity<PresignUrlResponseDTO> getPresignedUrl(@PathVariable String fileName) {
//         var presignedUrl = minioService.generatePresignedUrl(fileName);
//         return ResponseEntity.ok(presignedUrl);
//     }

//     @DeleteMapping("/{fileName}")
//     public ResponseEntity<Void> deleteFile(@PathVariable String fileName) {
//         minioService.deleteFile(fileName);
//         return ResponseEntity.ok().build();
//     }
// } 