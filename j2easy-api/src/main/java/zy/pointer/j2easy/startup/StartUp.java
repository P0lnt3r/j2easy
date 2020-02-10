package zy.pointer.j2easy.startup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("zy.pointer.j2easy")
@ServletComponentScan("zy.pointer") // Filter & Listener For (WebApplicationServer-Tomcat)
public class StartUp {

    public static void main(String[] args) {
        SpringApplication.run( StartUp.class , args );
    }

}
