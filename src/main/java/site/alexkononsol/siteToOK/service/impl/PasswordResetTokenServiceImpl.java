package site.alexkononsol.siteToOK.service.impl;

import org.springframework.stereotype.Service;
import site.alexkononsol.siteToOK.entity.PasswordResetToken;
import site.alexkononsol.siteToOK.repositories.PasswordResetTokenRepository;
import site.alexkononsol.siteToOK.service.PasswordResetTokenService;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
    private final PasswordResetTokenRepository repository;

    public PasswordResetTokenServiceImpl(PasswordResetTokenRepository repository) {
        this.repository = repository;
    }

    @Override
    public PasswordResetToken getByToken(String token) {
        return repository.findByToken(token);
    }
}
