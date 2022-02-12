package site.alexkononsol.siteToOK.service;

import site.alexkononsol.siteToOK.entity.PasswordResetToken;

public interface PasswordResetTokenService {
    PasswordResetToken getByToken(String token);
}
