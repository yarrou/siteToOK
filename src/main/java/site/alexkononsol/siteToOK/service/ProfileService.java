package site.alexkononsol.siteToOK.service;

import site.alexkononsol.siteToOK.entity.Profile;

public interface ProfileService {
    byte[] getProfileImageById(Long id);
    void save(Profile profile);
}
