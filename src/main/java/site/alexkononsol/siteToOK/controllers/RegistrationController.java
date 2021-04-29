package site.alexkononsol.siteToOK.controllers;

import site.alexkononsol.siteToOK.entity.User;
import site.alexkononsol.siteToOK.repositories.UserRepository;
import site.alexkononsol.siteToOK.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository repository;

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
    public String addUser(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult, ModelMap model,HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException{

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
        if (repository.existsByUsername(userForm.getUsername())){
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "registration";
        }
        userService.register(userForm, getSiteURL(request));
        model.addAttribute("usr_email",userForm.getEmail());

        return "redirect:/registration_success";
    }
    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
    @GetMapping("/verify")
    public String verifyUser(@RequestParam (required = true) String code) {
        if (userService.verify(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }

}
