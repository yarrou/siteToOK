package org.alex.konon.sol.siteToOK.controllers;

import org.alex.konon.sol.siteToOK.entity.PasswordResetToken;
import org.alex.konon.sol.siteToOK.entity.User;
import org.alex.konon.sol.siteToOK.form.ForgotPasswordForm;
import org.alex.konon.sol.siteToOK.repositories.PasswordResetTokenRepository;
import org.alex.konon.sol.siteToOK.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Controller
@RequestMapping("/forgot-password")
public class PasswordForgotController {

    @Autowired
    private UserService userService;
    @Autowired private PasswordResetTokenRepository tokenRepository;
    //@Autowired private EmailService emailService;

    /*@ModelAttribute("forgotPasswordForm")
    public PasswordForgotDto forgotPasswordDto() {
        return new PasswordForgotDto();
    }*/

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @GetMapping
    public String displayForgotPasswordPage(ModelMap model) {
        model.addAttribute("form",new ForgotPasswordForm());
        return "forgot-password";
    }

    @PostMapping
    public String processForgotPassword(@ModelAttribute("form")  ForgotPasswordForm form,
                                        HttpServletRequest request, ModelMap model) throws UnsupportedEncodingException, MessagingException {

        User user = userService.findByEmail(form.getEmail());
        if (user == null){
            model.addAttribute("emailError","пользователь с таким email не зарегистрирован");
            return "forgot-password";
        }
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(30);
        tokenRepository.save(token);
        userService.forgotPasswordEmail(form,getSiteURL(request),token);

        return "resetPasswordSuccess";

    }

}
