package site.alexkononsol.siteToOK.controllers;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import site.alexkononsol.siteToOK.service.ProfileService;

@Controller
public class ProfileImageController {
    private final ProfileService service;

    public ProfileImageController(ProfileService service) {
        this.service = service;
    }

    @GetMapping(value = "/profile_image/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    Resource viewImage(@PathVariable Long imageId) {
        byte[] image = service.getProfileImageById(imageId);
        return new ByteArrayResource(image);
    }
}
