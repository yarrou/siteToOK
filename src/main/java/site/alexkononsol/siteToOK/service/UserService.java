package site.alexkononsol.siteToOK.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import site.alexkononsol.siteToOK.entity.User;

public interface UserService {
    void register(User user);
    boolean userPasswordForgot(String email);
    boolean verifyRegistration(String verificationCode);
    User getUserByName(String name);
    boolean saveUser(User user);
    boolean deleteUser(Long userId);
    User findUserByEmail(String email);
    User findUserById(Long userId);
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
