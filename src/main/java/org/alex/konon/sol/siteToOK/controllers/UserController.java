package org.alex.konon.sol.siteToOK.controllers;

import org.alex.konon.sol.siteToOK.entity.Profile;
import org.alex.konon.sol.siteToOK.entity.User;
import org.alex.konon.sol.siteToOK.repositories.ProfileRepository;
import org.alex.konon.sol.siteToOK.repositories.UserRepository;
import org.alex.konon.sol.siteToOK.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.Principal;

@Controller
public class  UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    UserService userService;

    @GetMapping("/my_profile")
    public String myProfile(ModelMap model, Principal principal){
        User user = userRepository.findByUsername(principal.getName());
        Profile profile = user.getProfile();
        model.addAttribute("user",user);
        model.addAttribute("profile",profile);
        return "my_profile";
    }

    @GetMapping("/my_profile_editor")
    public String myProfileEditor(ModelMap model, Principal principal){
        User user = userRepository.findByUsername(principal.getName());
        Profile profile = user.getProfile();
        model.addAttribute("user",user);
        model.addAttribute("profile",profile);
        return "my_profile_editor";
    }

    @PostMapping("/my_profile_editor")
    public ModelAndView editingMyProfile(ModelMap model, @ModelAttribute("profile") Profile profile, @RequestParam("photo") MultipartFile multipartImage){
        String returnPage = null;
        if(multipartImage!=null) {
            try {
                profile.setContent(multipartImage.getBytes());
                returnPage = "redirect:/my_profile";
            } catch (IOException e) {
                model.addAttribute("clarification","не удается считать файл");
                model.addAttribute("errorMsg", e.getMessage());
                returnPage = "error";
            }
        }
        else {
            returnPage = "error";
            model.addAttribute("clarification","произошла ошибка загрузки файла");
        }
        profileRepository.save(profile);
        return new ModelAndView(returnPage,model);
    }
    @PostConstruct
    public void init() {
        userService.adminInit();
    }
}
