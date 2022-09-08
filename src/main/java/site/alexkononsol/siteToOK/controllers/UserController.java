package site.alexkononsol.siteToOK.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import site.alexkononsol.siteToOK.entity.Profile;
import site.alexkononsol.siteToOK.entity.User;
import site.alexkononsol.siteToOK.service.ImageS3Service;
import site.alexkononsol.siteToOK.service.ProfileService;
import site.alexkononsol.siteToOK.service.UserService;
import site.alexkononsol.siteToOK.service.impl.UserServiceImpl;

import javax.annotation.PostConstruct;
import java.security.Principal;

@AllArgsConstructor
@Slf4j
@Controller
public class  UserController {
    private final ProfileService profileService;
    private final UserService userService;
    private final ImageS3Service imageS3Service;

    @GetMapping("/my_profile")
    public String myProfile(ModelMap model, Principal principal){
        User user = userService.getUserByName(principal.getName());
        Profile profile = user.getProfile();
        model.addAttribute("user",user);
        model.addAttribute("profile",profile);
        return "my_profile";
    }

    @GetMapping("/my_profile_editor")
    public String myProfileEditor(ModelMap model, Principal principal){
        User user = userService.getUserByName(principal.getName());
        Profile profile = user.getProfile();
        model.addAttribute("user",user);
        model.addAttribute("profile",profile);
        return "my_profile_editor";
    }

    @PostMapping("/my_profile_editor")
    public ModelAndView editingMyProfile(ModelMap model, @ModelAttribute("profile") Profile profile, @RequestParam("photo") MultipartFile multipartImage){
        String returnPage = "error";
        if(multipartImage!=null) {
            try {
                profile.setAvatarLink(imageS3Service.saveImageInS3(multipartImage));
                returnPage = "redirect:/my_profile";
            } catch (Exception e) {
                log.error("не удается прочесть файл",e);
                model.addAttribute("clarification","не удается считать файл");
                model.addAttribute("errorMsg", e.getMessage());
                returnPage = "error";
            }
        }
        else {
            log.error("произошла ошибка загрузки файла");
            model.addAttribute("clarification","произошла ошибка загрузки файла");
        }
        profileService.save(profile);
        return new ModelAndView(returnPage,model);
    }
    @PostConstruct
    public void init() {
        userService.adminInit();
        ((UserServiceImpl)userService).changeAllProfiles();
    }
}
