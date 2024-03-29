package site.alexkononsol.siteToOK.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import site.alexkononsol.siteToOK.service.impl.UserServiceImpl;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .requiresChannel()
                .anyRequest()
                .requiresSecure()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                //Доступ только для не зарегистрированных пользователей
                .antMatchers("/registration","/forgot-password","/registration_success","/reset-password","/resetPasswordSuccess").not().fullyAuthenticated()
                //Доступ только для пользователей с ролью Администратор
                .antMatchers("/admin/**", "/add_article", "/redactor_article", "/delete_article","/elements","/elements/**").hasRole("ADMIN")
                //Доступ для авторизованных пользователей
                .antMatchers( "/my_profile","/my_profile_editor","/messages","/messages/**","/review_add").fullyAuthenticated()
                //Доступ разрешен всем пользователей
                .antMatchers("/", "/tipy_tela","/results", "/training","/article","/verify_fail","/verify_success","/olga_kononovich_more","/generic","/sports_supplements","/nutrition").permitAll()
                .antMatchers("/**","/.svg", "/.ico", "/.eot", "/.woff2",
                        "/.ttf", "/.woff", "/.html", "/.js",
                        "/.map", "/*.bundle.*").permitAll()
                //Все остальные страницы требуют аутентификации
                .anyRequest().authenticated()

                .and()

                //Настройка для входа в систему
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login_security_check")
                .failureUrl("/login?error")
                .usernameParameter("username")
                .passwordParameter("password")
                //redirect на главную страницу после успешного входа
                //.defaultSuccessUrl("/site")
                .permitAll()

                .and()

                .logout()
                .permitAll()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)

                .and()
                .logout().deleteCookies("JSESSIONID")

                .and()
                .rememberMe().key("uniqueAndSecret")
        ;
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth, UserServiceImpl userService) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder());
    }
}
