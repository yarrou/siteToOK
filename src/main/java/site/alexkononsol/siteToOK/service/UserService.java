package site.alexkononsol.siteToOK.service;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import site.alexkononsol.siteToOK.entity.PasswordResetToken;
import site.alexkononsol.siteToOK.entity.Profile;
import site.alexkononsol.siteToOK.entity.Role;
import site.alexkononsol.siteToOK.entity.User;
import site.alexkononsol.siteToOK.form.ForgotPasswordForm;
import site.alexkononsol.siteToOK.repositories.PasswordResetTokenRepository;
import site.alexkononsol.siteToOK.repositories.ProfileRepository;
import site.alexkononsol.siteToOK.repositories.RoleRepository;
import site.alexkononsol.siteToOK.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Slf4j
@Service
public class UserService implements UserDetailsService {
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
    private JavaMailSender mailSender;
    @Value("${S_EMAIL}")
    private String defaultEmail;
    @Value("${ADMIN_NAME}")
    private String adminName;
    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    public void forgotPasswordEmail(ForgotPasswordForm form, String siteURL, PasswordResetToken token) throws MessagingException, UnsupportedEncodingException{
        User user = findByEmail(form.getEmail());
        log.info("запрос сброса пароля для юзера {}",user.getUsername());
        String toAddress = form.getEmail();
        String fromAddress = defaultEmail;
        String senderName = "FitnessToAll";
        String subject = "Вы запросили сброс пароля";
        String content = "Уважаемый(ая) [[name]],<br>"
                + "Пожалуйста, пройдите по ссылке для сброса пароля<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">СБРОСИТЬ ПАРОЛЬ</a></h3>"
                + "Спасибо,<br>"
                + "Fitness To All.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getUsername());
        String forgotPasswordURL = siteURL + "/reset-password?token=" + token.getToken();

        content = content.replace("[[URL]]", forgotPasswordURL);

        helper.setText(content, true);

        mailSender.send(message);
        log.debug("отправлена ссылка для сброса пароля на email {}",user.getEmail());
    }


    public void register(User user, String siteURL) throws UnsupportedEncodingException, MessagingException {
        log.debug("регистрация пользователя {}",user.getUsername());
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setEnabled(false);
        saveUser(user);
        sendVerificationEmail(user, siteURL);
    }

    private void sendVerificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
        log.debug("отправка ссылки на подтверждение регистрацию на email {}",user.getEmail());
        String toAddress = user.getEmail();
        String fromAddress = defaultEmail;
        String senderName = "FitnessToAll";
        String subject = "Please verify your registration";
        String content = "Уважаемый(ая) [[name]],<br>"
                + "Пожалуйста, пройдите по ссылке для подтверждения регистрации<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">ПОДТВЕРДИТЬ РЕГИСТРАЦИЮ</a></h3>"
                + "Спасибо,<br>"
                + "Fitness To All.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getUsername());
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }
    public boolean verify(String verificationCode) {
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

    public User findUserById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new User());
    }

    public List<User> allUsers() {
        return userRepository.findAll();
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

    public List<User> usergtList(Long idMin) {
        return em.createQuery("SELECT u FROM User u WHERE u.id > :paramId", User.class)
                .setParameter("paramId", idMin).getResultList();
    }

    public User findByEmail(String email){

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
}