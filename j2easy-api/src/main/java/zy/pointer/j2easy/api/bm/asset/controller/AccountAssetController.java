package zy.pointer.j2easy.api.bm.asset.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bm/asset/accountasset")
@Api("账户资产")
public class AccountAssetController {

    @GetMapping("/test1")
    @ApiOperation("测试方法1")
    public int test1(){
        return 1;
    }

    @GetMapping("/test2")
    public int test2(){
        return 1;
    }

}
