package zy.pointer.j2easy.startup.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {

    /**
     * Swagger2 请求资源路径匹配表达式, 用于在 Shiro 中使用 anon 许可匿名访问 , Log 拦截器免拦截.
     */
    public final static String[] SWAGGER2_RESOURCES_PATHPATTERN = {
            "/swagger-ui.html" ,
            "/swagger-resources/**" ,
            "/v2/api-docs/**" ,
            "/webjars/springfox-swagger-ui/**"
    } ;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("zy.pointer.j2easy.api"))
                .paths(PathSelectors.any())
                .build().apiInfo(new ApiInfoBuilder()
                        .title("接口文档")
                        .description("users : 用户相关接口")
                        .version("0.0.0")
                        .build());
    }

}
