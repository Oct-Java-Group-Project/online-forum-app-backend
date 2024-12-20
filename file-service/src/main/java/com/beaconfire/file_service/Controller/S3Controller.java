package com.beaconfire.file_service.Controller;

import com.beaconfire.file_service.DTO.FileRequestResponse;
import com.beaconfire.file_service.DTO.FileUploadResponse;
import com.beaconfire.file_service.Service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;

@RestController
@RequestMapping("/files")
@Tag(name = "Fire Microservice", description = "APIs for file upload and download")
public class S3Controller {
   private final String SUCCESS_MESSAGE = "File successfully uploaded";
   private final String FAILURE_MESSAGE = "File could not be uploaded";

   private final S3Service s3Service;

   @Autowired
   public S3Controller(S3Service s3Service) {
      this.s3Service = s3Service;
   }

   @GetMapping
   public ResponseEntity<String> test() {
      return ResponseEntity.ok().body("TEST!!!");
   }

   @PostMapping("/upload")
   @Operation(summary = "Upload a file", description = "Uploads a file to AWS S3 and returns the file URL.")
   public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile file) {
      try{
         String url = s3Service.uploadFile(file);
         FileUploadResponse response = FileUploadResponse.builder()
               .message(SUCCESS_MESSAGE)
               .objectUrl(url)
               .build();
         return ResponseEntity.ok(response);
      }
      catch(Exception e){
         FileUploadResponse errorResponse = FileUploadResponse.builder()
               .message(FAILURE_MESSAGE)
               .objectUrl(null)
               .build();
         return ResponseEntity.status(500).body(errorResponse);
      }
   }

   @GetMapping("/download")
   @Operation(summary = "Download a file", description = "Downloads a file from AWS S3 based on the given object URL.")
   public ResponseEntity<byte[]> downloadFile(@RequestParam("objectUrl") String objectUrl) {
      try{
         FileRequestResponse response = s3Service.downloadFile(objectUrl);
         String fileName = extractKeyFromUrl(objectUrl);

         return ResponseEntity.ok()
               .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
               .contentType(MediaType.parseMediaType(response.getContentType()))
               .body(response.getContent());
      }
      catch(Exception e){
         return ResponseEntity.status(500).body(null);
      }
   }

   private String extractKeyFromUrl(String url) {
      int index = url.lastIndexOf("/");
      if(index < 0){
         throw new IllegalArgumentException("Invalid URL");
      }
      return url.substring(index + 1);
   }
}
