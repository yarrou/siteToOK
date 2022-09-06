package site.alexkononsol.siteToOK.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageS3Service {
    public String saveImageInS3(MultipartFile file);
}
