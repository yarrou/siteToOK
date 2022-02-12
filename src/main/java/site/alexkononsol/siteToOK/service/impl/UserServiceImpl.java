package site.alexkononsol.siteToOK.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import site.alexkononsol.siteToOK.entity.PasswordResetToken;
import site.alexkononsol.siteToOK.entity.Profile;
import site.alexkononsol.siteToOK.entity.Role;
import site.alexkononsol.siteToOK.entity.User;
import site.alexkononsol.siteToOK.repositories.PasswordResetTokenRepository;
import site.alexkononsol.siteToOK.repositories.RoleRepository;
import site.alexkononsol.siteToOK.repositories.UserRepository;
import site.alexkononsol.siteToOK.service.EmailSenderService;
import site.alexkononsol.siteToOK.service.UserService;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder encoder;
    private final EmailSenderService emailSenderService;
    private final String defaultEmail;
    private final String adminName;
    private final String adminPassword;

    public UserServiceImpl(PasswordResetTokenRepository passwordResetTokenRepository,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           BCryptPasswordEncoder encoder,
                           EmailSenderService emailSenderService,
                           @Value("${S_EMAIL}") String defaultEmail,
                           @Value("${ADMIN_NAME}") String adminName,
                           @Value("${ADMIN_PASSWORD}") String adminPassword) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.emailSenderService = emailSenderService;
        this.defaultEmail = defaultEmail;
        this.adminName = adminName;
        this.adminPassword = adminPassword;
    }

    @Override
    public void register(User user) {
        log.debug("регистрация пользователя {}", user.getUsername());
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setEnabled(false);
        saveUser(user);
        emailSenderService.sendVerificationEmail(user);
    }

    @Override
    public boolean verifyRegistration(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            log.warn("registration denied");
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepository.save(user);
            log.debug("user {} registered successfully", user.getUsername());
            return true;
        }
    }

    @Override
    public long userCount() {
        return userRepository.count();
    }

    @Override
    public List<User> get5Users(int page) {
        return userRepository.fiveUsers(page);
    }

    @Override
    public User findUserById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new User());
    }

    @Override
    public boolean saveUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());
        if (userFromDB != null) {
            log.error("user named {} already exists", user.getUsername());
            return false;
        }
        Profile profile = new Profile();
        user.setProfile(profile);
        profile.setUser(user);
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        userRepository.save(user);
        log.info("user {} saved successfully", user.getUsername());
        return true;
    }

    @Override
    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            log.warn("deleted user with id {}", userId);
            return true;
        }
        log.warn("deleting a user with id {} is impossible, absent in the database", userId);
        return false;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    /* This method is for automatic database configuration. This includes creating and saving the ADMIN and USER
    roles, and an account with administrator privileges. Login and password are set using the {ADMIN_NAME} and
    {ADMIN_PASSWORD} environment variables. */
    @Override
    public void adminInit() {
        if (!userRepository.existsByUsername(adminName)) {
            log.warn("default administrator account not found (username is {} ), database initialization", adminName);
            Role userRole = new Role();
            userRole.setId(1L);
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            adminRole.setId(2L);
            roleRepository.save(adminRole);
            User user = new User();
            user.setUsername(adminName);
            Profile profile = new Profile();
            user.setProfile(profile);
            profile.setUser(user);
            user.setEmail(defaultEmail);
            user.setEnabled(true);
            Set<Role> roles = new HashSet<>();
            roles.add(new Role(1L, "ROLE_USER"));
            roles.add(new Role(2L, "ROLE_ADMIN"));
            user.setRoles(roles);
            user.setPassword(encoder.encode(adminPassword));
            userRepository.save(user);
        }
    }

    @Override
    public void addAdminRole(Long userId) {
        User user = userRepository.getOne(userId);
        Role role = roleRepository.findByName("ROLE_ADMIN");
        Set<Role> roles = user.getRoles();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
        log.warn("the user {} is granted admin privileges", user.getUsername());
    }

    @Override
    public void upgradePassword(String token, String password) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);
        User user = resetToken.getUser();
        userRepository.updatePassword(encoder.encode(password), user.getId());
        passwordResetTokenRepository.delete(resetToken);
        log.debug("update password user {}",user.getUsername() );
    }

    @Override
    public boolean existsByName(String name) {
        return userRepository.existsByUsername(name);
    }


    @Override
    public boolean userPasswordForgot(String email) {
        User user = findUserByEmail(email);
        if (user == null) {
            return false;
        }
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(30);
        passwordResetTokenRepository.save(token);
        emailSenderService.forgotPasswordEmail(user, token);
        return true;
    }

    @Override
    public User getUserByName(String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            UsernameNotFoundException e = new UsernameNotFoundException("User not found");
            log.error("user {} not found", username, e);
            throw e;
        }
        log.debug("user {} found ", username);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByName(username);
    }
}