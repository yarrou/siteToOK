package site.alexkononsol.siteToOK.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.alexkononsol.siteToOK.service.ImageS3Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class ImageS3ServiceImpl implements ImageS3Service {
    @Autowired
    AmazonS3 s3Client;

    @Value("${do.home.folder}")
    private String pathFolder = "siteOK/dev/";

    @Value("${do.space.bucket}")
    private String doSpaceBucket;

    @Override
    public String saveImageInS3(MultipartFile file) {
        ObjectMetadata data = new ObjectMetadata();
        data.setContentType(file.getContentType());
        data.setContentLength(file.getSize());
        String filepath = pathFolder + UUID.randomUUID() + ".jpg";
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(doSpaceBucket,
                    filepath, file.getInputStream(), data).withCannedAcl(CannedAccessControlList.PublicRead);
            s3Client.putObject(putObjectRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return s3Client.getUrl(doSpaceBucket, filepath).toString();
    }

    @Override
     public void deleteImageInS3(String name) {
        System.out.println("deleted photo " + name);
        s3Client.deleteObject(doSpaceBucket,pathFolder + name);
    }
}
