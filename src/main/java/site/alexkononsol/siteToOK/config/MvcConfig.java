package site.alexkononsol.siteToOK.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
    }

}