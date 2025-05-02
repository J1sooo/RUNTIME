package com.est.runtime.post.img;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@RestController
public class ImageController {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final S3Client s3Client;

    public ImageController(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @PostMapping("/api/image/upload")
    public ResponseEntity<Map<String, String>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "isBase64", required = false, defaultValue = "false") boolean isBase64  // base64 인코딩 여부를 결정하는 파라미터 (기본값 false)
    ) throws IOException {

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String imageUrl;

        if (isBase64) {

            byte[] imageBytes = Base64.getDecoder().decode(new String(file.getBytes()));

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(imageBytes));

            imageUrl = String.format("https://%s.s3.amazonaws.com/%s", bucketName, fileName);
        } else {

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            imageUrl = String.format("https://%s.s3.amazonaws.com/%s", bucketName, fileName);
        }

        return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
    }
}
