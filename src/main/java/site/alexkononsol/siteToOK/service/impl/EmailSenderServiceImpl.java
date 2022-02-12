package site.alexkononsol.siteToOK.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import site.alexkononsol.siteToOK.emailSender.EmailSender;
import site.alexkononsol.siteToOK.entity.PasswordResetToken;
import site.alexkononsol.siteToOK.entity.User;
import site.alexkononsol.siteToOK.service.EmailSenderService;
import site.alexkononsol.siteToOK.service.MessagesSourcesService;
import site.alexkononsol.siteToOK.util.Endpoints;
import site.alexkononsol.siteToOK.util.MyRequestContextListener;

@Slf4j
@Service
public class EmailSenderServiceImpl implements EmailSenderService {


    private final String serverEmail;
    private final JavaMailSender mailSender;
    private final MyRequestContextListener requestContextListener;
    private final MessagesSourcesService messagesSourcesService;


    public EmailSenderServiceImpl(MyRequestContextListener requestContextListener, JavaMailSender mailSender, MessagesSourcesService messagesSourcesService,@Value("${S_EMAIL}")String serverEmail) {
        this.requestContextListener = requestContextListener;
        this.messagesSourcesService = messagesSourcesService;
        this.mailSender = mailSender;
        this.serverEmail = serverEmail;
    }

    @Override
    public void sendVerificationEmail(User user) {
        String url = requestContextListener.getMyUrl()+ Endpoints.CONFIRM_REGISTRATION + "?code=" + user.getVerificationCode();
        String emailSubject = messagesSourcesService.getStringValue(messagesSourcesService.EMAIL_REGISTRATION_VERIFY_SUBJECT);
        String content = messagesSourcesService.getStringValue(messagesSourcesService.EMAIL_REGISTRATION_VERIFY_TEXT);
        content = content.replace("[[name]]", user.getUsername());
        content = content.replace("[[URL]]",url);
        new EmailSender(user.getEmail(),content,emailSubject,mailSender,serverEmail);
    }

    @Override
    public void forgotPasswordEmail(User user, PasswordResetToken token) {
        String url = requestContextListener.getMyUrl() + Endpoints.RESET_PASSWORD + "?token=" + token.getToken();
        String emailSubject = messagesSourcesService.getStringValue(messagesSourcesService.EMAIL_FORGOT_PASSWORD_SUBJECT);
        String content = messagesSourcesService.getStringValue(messagesSourcesService.EMAIL_FORGOT_PASSWORD_TEXT);
        content = content.replace("[[name]]", user.getUsername());
        content = content.replace("[[URL]]",url);
        new EmailSender(user.getEmail(),content,emailSubject,mailSender,serverEmail);
    }
}
