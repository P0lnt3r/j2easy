package zy.pointer.j2easy.api.publics;

import cn.hutool.json.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/publics")
@Api("PUBLICS")
public class PublicsController {

    @PostMapping("/checkCode")
    @ApiOperation("远程检测 Code 合法性")
    @ApiImplicitParams({
            @ApiImplicitParam(  )
    })
    public Object checkCode( String code ){
        JSONObject obj = new JSONObject();
        obj.put("jwt","JWT:kfklaslkkl31430219jjf0d");
        return obj;
    }

}
