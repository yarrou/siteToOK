package site.alexkononsol.siteToOK.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import site.alexkononsol.siteToOK.entity.PasswordResetToken;
import site.alexkononsol.siteToOK.entity.User;
import site.alexkononsol.siteToOK.form.PasswordResetForm;
import site.alexkononsol.siteToOK.repositories.PasswordResetTokenRepository;
import site.alexkononsol.siteToOK.service.impl.UserServiceImpl;

import javax.validation.Valid;

@Controller
@RequestMapping("/reset-password")
public class PasswordResetController {

    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @ModelAttribute("passwordResetForm")
    public PasswordResetForm passwordReset() {
        return new PasswordResetForm();
    }

    @GetMapping
    public String displayResetPasswordPage(@RequestParam(required = false) String token,
                                           Model model) {

        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken == null){
            model.addAttribute("error", "Could not find password reset token.");
        } else if (resetToken.isExpired()){
            model.addAttribute("error", "Token has expired, please request a new password reset.");
        } else {
            model.addAttribute("token", resetToken.getToken());
        }

        return "reset-password";
    }

    @PostMapping
    @Transactional
    public String handlePasswordReset(@ModelAttribute("passwordResetForm") @Valid PasswordResetForm form,
                                      BindingResult result,
                                      RedirectAttributes redirectAttributes) {

        if (result.hasErrors()){
            redirectAttributes.addFlashAttribute(BindingResult.class.getName() + ".passwordResetForm", result);
            redirectAttributes.addFlashAttribute("passwordResetForm", form);
            return "redirect:/reset-password?token=" + form.getToken();
        }

        PasswordResetToken token = tokenRepository.findByToken(form.getToken());
        User user = token.getUser();
        String updatedPassword = passwordEncoder.encode(form.getPassword());
        userServiceImpl.updatePassword(updatedPassword, user.getId());
        tokenRepository.delete(token);

        return "redirect:/login?resetSuccess";
    }

}