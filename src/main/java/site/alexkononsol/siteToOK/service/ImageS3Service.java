package site.alexkononsol.siteToOK.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageS3Service {
    String saveImageInS3(MultipartFile file);

    void deleteImageInS3(String name);
}
