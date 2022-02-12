package site.alexkononsol.siteToOK.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import site.alexkononsol.siteToOK.form.ForgotPasswordForm;
import site.alexkononsol.siteToOK.service.UserService;

@Controller
@RequestMapping("/forgot-password")
public class PasswordForgotController {


    private final UserService userService;

    public PasswordForgotController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String displayForgotPasswordPage(ModelMap model) {
        model.addAttribute("form",new ForgotPasswordForm());
        return "forgot-password";
    }

    @PostMapping
    public String processForgotPassword(@ModelAttribute("form")  ForgotPasswordForm form,
                                         ModelMap model) {
        if(!userService.userPasswordForgot(form.getEmail())){
            model.addAttribute("emailError","пользователь с таким email не зарегистрирован");
            return "forgot-password";
        }else return "resetPasswordSuccess";

    }

}
