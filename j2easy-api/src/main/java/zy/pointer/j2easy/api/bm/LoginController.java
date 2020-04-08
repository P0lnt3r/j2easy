package zy.pointer.j2easy.api.bm;

import cn.hutool.json.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zy.pointer.j2easy.business.commons.ErrorCode;
import zy.pointer.j2easy.business.system.entity.Account;
import zy.pointer.j2easy.business.system.service.IAccountService;
import zy.pointer.j2easy.endecrypt.RSA;
import zy.pointer.j2easy.framework.auth.components.JWT;
import zy.pointer.j2easy.framework.auth.jwt.JWTModel;
import zy.pointer.j2easy.framework.endecrypt.RSAUtils;
import zy.pointer.j2easy.framework.exception.BusinessException;

import javax.validation.constraints.NotEmpty;

@RestController
@Api("后台登录接口")
@RequestMapping("/api/bm")
@Validated
public class LoginController {

    @Autowired
    IAccountService accountService;

    @Autowired
    JWT jwt;

    @PostMapping( "/login" )
    @ApiImplicitParams( {
            @ApiImplicitParam( paramType="query", name="username", dataType="String",  required=true, value="用户名" ),
            @ApiImplicitParam( paramType="query", name="password", dataType="String",  value="密码"   )
    } )
    public Object login( @NotEmpty( message = "请填写用户名") String username , @NotEmpty( message = "请填写密码" ) String password){
        try {
            username = RSA.decrypt( username );
            password = RSA.decrypt( password );
        } catch (Exception e) {
            throw new BusinessException( ErrorCode.ERROR_CODE_1006 );
        }
        Account account = accountService.login( username , password , 1 );
        // 颁发JWT
        JSONObject json = new JSONObject();
        String _ = jwt.issue(new JWTModel( account.getId() + "" , "realm:bm" , 120 ));
        json.put("jwt",_);

        return json;
    }

}
