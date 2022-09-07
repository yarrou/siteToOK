package site.alexkononsol.siteToOK.util;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;

public class BASE64DecodedMultipartFile implements MultipartFile {
    private final byte[] imgContent;
    private final String nameFile;

    public BASE64DecodedMultipartFile(byte[] imgContent,String nameFile) {
        this.imgContent = imgContent;
        this.nameFile = nameFile;
    }

    @Override
    public String getName() {

        return nameFile;
    }

    @Override
    public String getOriginalFilename() {
        return nameFile;
    }

    @Override
    public String getContentType() {
        return "image/png";
    }

    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }

    @Override
    public long getSize() {
        return imgContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return imgContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(imgContent);
    }

    @Override
    public Resource getResource() {
        return MultipartFile.super.getResource();
    }

    @Override
    public void transferTo(Path dest) throws IOException, IllegalStateException {
        MultipartFile.super.transferTo(dest);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(imgContent);
    }
}