package zy.pointer.j2easy.api.bm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zy.pointer.j2easy.business.commons.ErrorCode;
import zy.pointer.j2easy.framework.commons.IErrorCode;
import zy.pointer.j2easy.framework.exception.BusinessException;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@Api("后台登录接口")
@RequestMapping("/bm")
public class LoginController {

    @PostMapping( "/login" )
    @ApiImplicitParams( {
            @ApiImplicitParam( paramType="query", name="username", dataType="String", required=true, value="用户名" ),
            @ApiImplicitParam( paramType="query", name="password", dataType="String",  value="密码" )
    } )
    public Object login(@Valid @NotEmpty String username , @Valid @NotEmpty String password){
        System.out.println("username:" + username);
        System.out.println("password:" + password);
        throw new BusinessException( ErrorCode.ERROR_CODE_1006 );
    }

}
