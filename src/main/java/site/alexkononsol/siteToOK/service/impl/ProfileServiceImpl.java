package site.alexkononsol.siteToOK.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.alexkononsol.siteToOK.entity.Profile;
import site.alexkononsol.siteToOK.repositories.ProfileRepository;
import site.alexkononsol.siteToOK.service.ProfileService;

@AllArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository repository;

    @Override
    public byte[] getProfileImageById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
                .getContent();
    }

    @Override
    public void save(Profile profile) {
        repository.save(profile);
    }
}
