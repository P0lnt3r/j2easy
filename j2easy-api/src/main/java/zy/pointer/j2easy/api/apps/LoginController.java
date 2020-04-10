package zy.pointer.j2easy.api.apps;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping( "/api/apps/" )
@RestController( value = "AppLoginController")
public class LoginController {

    @PostMapping( "/login" )
    public int login(){
        return 1;
    }

}
