package zy.pointer.j2easy.startup.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import zy.pointer.j2easy.framework.web.interceptors.IdentificationInterceptor;
import zy.pointer.j2easy.framework.web.interceptors.LogTraceBuildInterceptor;
import zy.pointer.j2easy.framework.web.interceptors.RepeatRequestProtectInterceptor;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Autowired
    MessageSource messageSource;

    @Autowired
    LogTraceBuildInterceptor logTraceBuildInterceptor;

    @Autowired
    IdentificationInterceptor identificationInterceptor;

    @Autowired
    RepeatRequestProtectInterceptor repeatRequestProtectInterceptor;

    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .maxAge(3600)
                .allowCredentials(true);
    }

    @Override
    @Bean
    protected Validator getValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        try {
            validator.setValidationMessageSource( messageSource );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return validator;
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // LOG
        addMVCLogInterceptor( registry );
    }

    private void addMVCLogInterceptor( InterceptorRegistry registry ){
        List<String> logExcludePath = Arrays.asList( Swagger2Config.SWAGGER2_RESOURCES_PATHPATTERN );
        registry.addInterceptor( logTraceBuildInterceptor )
                .excludePathPatterns(logExcludePath).addPathPatterns("/**");
        registry.addInterceptor( repeatRequestProtectInterceptor )
                .excludePathPatterns(logExcludePath).addPathPatterns("/**");
        registry.addInterceptor( identificationInterceptor )
                .excludePathPatterns(logExcludePath).addPathPatterns("/**");
    }

}
