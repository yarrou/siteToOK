package site.alexkononsol.siteToOK.emailSender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Slf4j
public class EmailSender implements Runnable{


    private JavaMailSender mailSender;
    private String serverEmail;
    private String siteName;

    private String toEmail;
    private String emailText;
    private String emailSubject;

    public EmailSender(String toEmail, String emailText, String emailSubject,JavaMailSender mailSender,String serverEmail) {
        this.serverEmail = serverEmail;
        this.mailSender = mailSender;
        this.toEmail = toEmail;
        this.emailText = emailText;
        this.emailSubject = emailSubject;

        new Thread(this).start();
    }

    private void sendEmail() throws MessagingException, UnsupportedEncodingException {
        String senderName = siteName;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(serverEmail, senderName);
        helper.setTo(toEmail);
        helper.setSubject(emailSubject);


        helper.setText(emailText, true);

        mailSender.send(message);

    }

    @Override
    public void run() {
        try {
            sendEmail();
        } catch (MessagingException e) {
            log.error("error when sending email",e);
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            log.error("error when sending email",e);
            e.printStackTrace();
        }
    }

}