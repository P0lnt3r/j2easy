package zy.pointer.j2easy.api.bm.cms.controller;

import io.swagger.annotations.Api;
import org.checkerframework.checker.units.qual.A;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("内容管理")
@RequestMapping("/api/v1/cms/content")
public class ContentController {

    @GetMapping("/test")
    public int test(){
        return 1;
    }

    @GetMapping("/test2")
    public int test2(){
        return 2;
    }

}
