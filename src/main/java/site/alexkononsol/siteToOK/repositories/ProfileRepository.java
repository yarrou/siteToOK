package site.alexkononsol.siteToOK.repositories;

import site.alexkononsol.siteToOK.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile,Long> {
}
