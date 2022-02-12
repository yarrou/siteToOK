package site.alexkononsol.siteToOK.service;

public interface MessagesSourcesService {

    public String getStringValue(String value);

    public static final String EMAIL_FORGOT_PASSWORD_SUBJECT = "email.forgot.password.subject";
    public static final String EMAIL_FORGOT_PASSWORD_TEXT ="email.forgot.password.text";
    public static final String EMAIL_REGISTRATION_VERIFY_SUBJECT = "email.registration.verify.subject";
    public static final String EMAIL_REGISTRATION_VERIFY_TEXT ="email.registration.verify.text";
}