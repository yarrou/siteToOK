package org.alex.konon.sol.siteToOK.repositories;

import org.alex.konon.sol.siteToOK.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile,Long> {
}
