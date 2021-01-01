package org.alex.konon.sol.siteToOK.controllers;

import org.alex.konon.sol.siteToOK.entity.Profile;
import org.alex.konon.sol.siteToOK.entity.Role;
import org.alex.konon.sol.siteToOK.entity.User;
import org.alex.konon.sol.siteToOK.repositories.ProfileRepository;
import org.alex.konon.sol.siteToOK.repositories.RoleRepository;
import org.alex.konon.sol.siteToOK.repositories.UserRepository;
import org.alex.konon.sol.siteToOK.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/admin")
    public String userList(Model model,@RequestParam(value = "page",defaultValue = "1") int page) {
        long countPagesWithUsers =1;
        long countUsersInDB = userRepository.count();
        ArrayList<User> users = userRepository.fiveUsers(page);
        Role admRole = roleRepository.findByName("ROLE_ADMIN");
        if(countUsersInDB%5 > 0){
            countPagesWithUsers = (countUsersInDB / 5) +1;
        }
        else {
            countPagesWithUsers = countUsersInDB / 5;
        }
        model.addAttribute("users", users);
        model.addAttribute("pageusers",page);
        model.addAttribute("countPages",countPagesWithUsers);
        model.addAttribute("admRole",admRole);
        return "admin";
    }

    @PostMapping("/admin")
    public String  deleteUser(@RequestParam(required = true, defaultValue = "" ) Long userId,
                              @RequestParam(required = true, defaultValue = "" ) String action,
                              Model model) {
        if (action.equals("delete")){
            userService.deleteUser(userId);
        }
        if (action.equals("addAdminRole")){
            userService.addAdminRole(userId);
        }
        return "redirect:/admin";
    }

    @GetMapping("/admin/gt/{userId}")
    public String  gtUser(@PathVariable("userId") Long userId, Model model) {
        model.addAttribute("allUsers", userService.usergtList(userId));
        return "admin";
    }

    @GetMapping("view_profile")
    public String viewProfile(ModelMap model, @RequestParam(required = true) Long profileId){
        User user = userService.findUserById(profileId);
        Profile profile = profileRepository.getOne(profileId);
        model.addAttribute("user",user);
        model.addAttribute("profile",profile);
        return "view_profile";
    }
}
