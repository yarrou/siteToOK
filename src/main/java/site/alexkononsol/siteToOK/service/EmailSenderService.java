package site.alexkononsol.siteToOK.service;

import site.alexkononsol.siteToOK.entity.PasswordResetToken;
import site.alexkononsol.siteToOK.entity.User;


public interface EmailSenderService {
    public void sendVerificationEmail(User user);
    public void forgotPasswordEmail(User user, PasswordResetToken token);
}
