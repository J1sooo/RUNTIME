package com.est.runtime.s3;

import com.est.runtime.post.Post;
import com.est.runtime.post.img.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImgUploadService {
    private final S3Client s3Client;

    @Value("${AWS_S3_BUCKET}")
    private String bucketName;

    public void uploadFiles(List<MultipartFile> files, Post post) throws IOException {

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue; // 빈 파일은 건너뜁니다.
            }
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            String imgUrl = String.format("https://%s.s3.amazonaws.com/%s", bucketName, fileName);

            Image img = new Image(fileName, imgUrl, post);
            post.addImg(img);
        }
    }

    public void deleteFile(List<Image> images) {
        for (Image img : images) {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(img.getFileName())
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        }
    }
}
