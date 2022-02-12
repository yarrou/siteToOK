package site.alexkononsol.siteToOK.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.alexkononsol.siteToOK.entity.User;
import site.alexkononsol.siteToOK.service.UserService;

import javax.validation.Valid;
import java.util.Locale;

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @GetMapping("/registration_success")
    public String registrationSuccess(@ModelAttribute("usr_email") String usrEmail){
        return "registration_success";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult, ModelMap model) {

        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())){
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "registration";
        }
        if(userForm.getUsername().toLowerCase(Locale.ROOT).equals("administration")){
            model.addAttribute("usernameError","Недопустимое имя пользователя");
            return "registration";
        }
        if (userService.existsByName(userForm.getUsername())){
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "registration";
        }
        userService.register(userForm);
        model.addAttribute("usr_email",userForm.getEmail());
        return "redirect:/registration_success";
    }
    @GetMapping("/verify")
    public String verifyUser(@RequestParam  String code) {
        return userService.verifyRegistration(code)?"verify_success":"verify_fail";
    }
}
