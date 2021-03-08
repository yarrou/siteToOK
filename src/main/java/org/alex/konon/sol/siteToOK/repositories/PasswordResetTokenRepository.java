package org.alex.konon.sol.siteToOK.repositories;

import org.alex.konon.sol.siteToOK.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    PasswordResetToken findByToken(String token);
    ArrayList<PasswordResetToken> findByUser_id(long id);


}
