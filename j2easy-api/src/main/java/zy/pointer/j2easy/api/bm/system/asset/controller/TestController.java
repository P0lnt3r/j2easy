package zy.pointer.j2easy.api.bm.system.asset.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping( "bm/system/asset" )
@RestController
public class TestController {

    @RequestMapping("/test")
    public Object test(){
        return "1";
    }

    @RequestMapping("/test2")
    @ApiOperation("一个憨批测试方法")
    public Object test2(){
        return "1";
    }

    @RequestMapping("/test3")
    @ApiOperation("又一个憨批测试方法")
    public Object test3(){
        return "1";
    }

}
