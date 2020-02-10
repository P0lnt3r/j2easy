package zy.pointer.j2easy.startup.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ConfigBean {

    @Value("${env}") String env;

}
