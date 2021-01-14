package org.alex.konon.sol.siteToOK.service;

import net.bytebuddy.utility.RandomString;
import org.alex.konon.sol.siteToOK.entity.PasswordResetToken;
import org.alex.konon.sol.siteToOK.entity.Profile;
import org.alex.konon.sol.siteToOK.entity.Role;
import org.alex.konon.sol.siteToOK.entity.User;
import org.alex.konon.sol.siteToOK.form.ForgotPasswordForm;
import org.alex.konon.sol.siteToOK.repositories.ProfileRepository;
import org.alex.konon.sol.siteToOK.repositories.RoleRepository;
import org.alex.konon.sol.siteToOK.repositories.UserRepository;
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

@Service
public class UserService implements UserDetailsService {
    @PersistenceContext
    private EntityManager em;
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
    }


    public void register(User user, String siteURL) throws UnsupportedEncodingException, MessagingException {
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setEnabled(false);
        saveUser(user);
        sendVerificationEmail(user, siteURL);
    }

    private void sendVerificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
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
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepository.save(user);

            return true;
        }

    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

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
            return false;
        }
        Profile profile = new Profile();
        user.setProfile(profile);
        profile.setUser(user);
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        userRepository.save(user);

        return true;
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
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
        userRepository.updatePassword(password, userId);
    }

    public void adminInit(){
        if(!userRepository.existsByUsername(adminName)){
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
    }

    public boolean isAdmin(User user){
        Role role = roleRepository.findByName("ROLE_ADMIN");
        Set<Role> roles = user.getRoles();
        return roles.contains(roles);
    }
}