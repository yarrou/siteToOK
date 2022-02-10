package site.alexkononsol.siteToOK.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
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
import site.alexkononsol.siteToOK.repositories.ProfileRepository;
import site.alexkononsol.siteToOK.repositories.RoleRepository;
import site.alexkononsol.siteToOK.repositories.UserRepository;
import site.alexkononsol.siteToOK.service.EmailSenderService;
import site.alexkononsol.siteToOK.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserDetailsService, UserService {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    private EmailSenderService emailSenderService;
    @Value("${S_EMAIL}")
    private String defaultEmail;
    @Value("${ADMIN_NAME}")
    private String adminName;
    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    public void register(User user) {
        log.debug("регистрация пользователя {}",user.getUsername());
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setEnabled(false);
        saveUser(user);
        emailSenderService.sendVerificationEmail(user);
    }

    public boolean verifyRegistration(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            log.warn("registration denied");
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepository.save(user);
            log.debug("user {} registered successfully",user.getUsername());
            return true;
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            UsernameNotFoundException e = new UsernameNotFoundException("User not found");
            log.error("user {} not found",username,e);
            throw e;
        }
        log.debug("user {} found ",username);
        return user;
    }

    @Override
    public long userCount() {
        return userRepository.count();
    }

    @Override
    public List<User> get5Users(int page) {
        return userRepository.fiveUsers(page);
    }

    public User findUserById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new User());
    }


    public boolean saveUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            log.error("user named {} already exists",user.getUsername());
            return false;
        }
        Profile profile = new Profile();
        user.setProfile(profile);
        profile.setUser(user);
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        userRepository.save(user);
        log.info("user {} saved successfully",user.getUsername());

        return true;
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            log.warn("deleted user with id {}",userId);
            return true;
        }
        log.warn("deleting a user with id {} is impossible, absent in the database",userId);
        return false;
    }

    public List<User> getUserList(Long idMin) {
        return em.createQuery("SELECT u FROM User u WHERE u.id > :paramId", User.class)
                .setParameter("paramId", idMin).getResultList();
    }

    public User findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public void updatePassword(String password, Long userId) {
        log.debug("update password user with id {}",userId);
        userRepository.updatePassword(password, userId);
    }

    /* This method is for automatic database configuration. This includes creating and saving the ADMIN and USER
    roles, and an account with administrator privileges. Login and password are set using the {ADMIN_NAME} and
    {ADMIN_PASSWORD} environment variables. */
    public void adminInit(){
        if(!userRepository.existsByUsername(adminName)){
            log.warn("default administrator account not found (username is {} ), database initialization",adminName);
            Role userRole =new Role();
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
            user.setPassword(bCryptPasswordEncoder.encode(adminPassword));
            userRepository.save(user);
        }
    }
    public void addAdminRole(Long userId){
        User user = userRepository.getOne(userId);
        Role role = roleRepository.findByName("ROLE_ADMIN");
        Set<Role> roles = user.getRoles();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
        log.warn("the user {} is granted admin privileges",user.getUsername());
    }

    public boolean isAdmin(User user){
        Role role = roleRepository.findByName("ROLE_ADMIN");
        Set<Role> roles = user.getRoles();
        return roles.contains(roles);
    }

    @Override
    public boolean userPasswordForgot(String email) {
        User user = findUserByEmail(email);
        if (user == null){
            return false;
        }
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(30);
        passwordResetTokenRepository.save(token);
        emailSenderService.forgotPasswordEmail(user,token);
        return true;
    }

    @Override
    public User getUserByName(String name) {
        return null;
    }
}