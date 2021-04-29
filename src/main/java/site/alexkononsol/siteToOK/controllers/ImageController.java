package site.alexkononsol.siteToOK.controllers;

import site.alexkononsol.siteToOK.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class ImageController {
    @Autowired
    ProfileRepository profileRepository;
    @GetMapping(value = "/profile_image/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    Resource viewImage(@PathVariable Long imageId) {
        byte[] image = profileRepository.findById(imageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
                .getContent();
        return new ByteArrayResource(image);
    }
}
