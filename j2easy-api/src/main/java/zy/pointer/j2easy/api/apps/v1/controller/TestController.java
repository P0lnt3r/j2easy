package zy.pointer.j2easy.api.apps.v1.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/apps/v1/test")
@RestController
@Api("APPS-TEST")
public class TestController {

    @GetMapping("/test")
    public int test(){
        return 1;
    }

}
