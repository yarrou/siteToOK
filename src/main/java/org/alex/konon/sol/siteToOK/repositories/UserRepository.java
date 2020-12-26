package org.alex.konon.sol.siteToOK.repositories;

import org.alex.konon.sol.siteToOK.entity.Article;
import org.alex.konon.sol.siteToOK.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    @Query(value = "SELECT * FROM users  order by id  LIMIT 5 OFFSET (:numberPage -1)*5 ",nativeQuery = true)
    ArrayList<User> fiveUsers(@Param("numberPage") long numberPage);

    @Query(value = "SELECT * FROM users  WHERE users.verification_code = :code",nativeQuery = true)
    public User findByVerificationCode(@Param("code") String code);
}
