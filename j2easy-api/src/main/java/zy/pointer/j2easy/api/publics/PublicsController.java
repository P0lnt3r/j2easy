package zy.pointer.j2easy.api.publics;

import cn.hutool.json.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zy.pointer.j2easy.api.publics.component.TianzeClient;
import zy.pointer.j2easy.api.publics.dto.UserVO;
import zy.pointer.j2easy.business.cms.entity.User;
import zy.pointer.j2easy.business.cms.service.IUserService;
import zy.pointer.j2easy.framework.auth.components.JWT;
import zy.pointer.j2easy.framework.auth.jwt.JWTModel;
import zy.pointer.j2easy.framework.commons.DateUtil;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/publics")
@Api("PUBLICS")
@Validated
public class PublicsController {

    @Autowired
    TianzeClient tianzeClient;

    @Autowired JWT jwtComponent;

    @Autowired
    IUserService userService;

    @PostMapping("/checkJWT")
    @ApiOperation("远程检测 JWT 合法性")
    @ApiImplicitParams({
            @ApiImplicitParam()
    })
    public Object checkJWT( String jwt ){
        JSONObject obj = new JSONObject();
        JWTModel model = jwtComponent.parse( jwt );
        User user = userService.findByUserId(Long.parseLong(model.getUid()));
        UserVO userVO = new UserVO();
        userVO.setId( user.getUserId() );
        userVO.setName(user.getName());
        userVO.setType( user.getType() );
        userVO.setRegistTime( user.getRegistTime().toString() );
        obj.put("jwt" , jwt);
        obj.put("user" , userVO);
        return obj;
    }


    @PostMapping("/checkCode")
    @ApiOperation("远程检测 Code 合法性")
    @ApiImplicitParams({
            @ApiImplicitParam()
    })
    public Object checkCode( @NotEmpty String code ){
        // 调用天泽系统获取
        UserVO userVO = tianzeClient.getUserMsg(code);
        if ( userVO == null ){
            return null;
        }
        User user = userService.findByUserId( userVO.getId() );
        if ( user == null ){
            user = new User();
        }
        user.setName(userVO.getName());
        user.setUserId( userVO.getId() );
        user.setType( userVO.getType() );
        user.setRegistTime( from( userVO.getRegistTime() ) );
        userService.saveOrUpdate( user );

        String jwtStr = jwtComponent.issue( new JWTModel( user.getUserId() + "" , "/" , 120 ));
        JSONObject obj = new JSONObject();
        obj.put("jwt",jwtStr);
        obj.put("user" , userVO);
        return obj;
    }

    private LocalDateTime from( String text ){
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return LocalDateTime.parse(
                text.replaceAll("T"," "),
                fmt
        );
    }

}
