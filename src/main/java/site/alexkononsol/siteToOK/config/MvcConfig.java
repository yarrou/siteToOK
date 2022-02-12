package site.alexkononsol.siteToOK.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/tipy_tela").setViewName("tipy_tela");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/olga_kononovich_more").setViewName("olga_kononovich_more");
        registry.addViewController("/generic").setViewName("generic");
        registry.addViewController("/elements").setViewName("elements");
        registry.addViewController("/elements/test").setViewName("test");
        registry.addViewController("/sports_supplements").setViewName("sports_supplements");
        registry.addViewController("/nutrition").setViewName("nutrition");
        registry.addViewController("/results").setViewName("results");
        registry.addViewController("/training").setViewName("training");
    }
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(new Locale("ru"));
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
    public LocaleChangeInterceptor localeChangeWithStringInterceptor(String lang) {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

}