package zy.pointer.j2easy.api.publics;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zy.pointer.j2easy.api.bm.cms.dto.DiscussDTO;
import zy.pointer.j2easy.api.bm.cms.dto.DiscussQueryDTO;
import zy.pointer.j2easy.api.bm.cms.vo.DiscussVO;
import zy.pointer.j2easy.api.publics.component.TianzeClient;
import zy.pointer.j2easy.api.publics.dto.UserVO;
import zy.pointer.j2easy.business.cms.entity.Discuss;
import zy.pointer.j2easy.business.cms.entity.User;
import zy.pointer.j2easy.business.cms.service.IDiscussService;
import zy.pointer.j2easy.business.cms.service.IUserService;
import zy.pointer.j2easy.framework.auth.components.JWT;
import zy.pointer.j2easy.framework.auth.jwt.JWTModel;
import zy.pointer.j2easy.framework.web.model.vo.PageVo;

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

    @Autowired
    IDiscussService discussService;

    @GetMapping("/getQuestions")
    @ApiOperation("获取问题列表")
    public PageVo<DiscussVO , Discuss> getQuestions(DiscussQueryDTO DTO){
        return new PageVo<DiscussVO , Discuss>().from(
                discussService.selectByMapForPage_extras( DTO.convert() , BeanUtil.beanToMap( DTO )) ,
                DiscussVO.class
        );
    }

    @GetMapping("/getReplies")
    @ApiOperation("获取回复列表")
    public PageVo<DiscussVO , Discuss> getReplies( DiscussQueryDTO DTO ){
//        DTO.setOrderProp("id");
//        DTO.setOrderMode("desc");
        return new PageVo<DiscussVO , Discuss>().from(
                discussService.selectByMapForPage_replies( DTO.convert() , BeanUtil.beanToMap( DTO )) ,
                DiscussVO.class
        );
    }

    @PostMapping("/reply")
    @ApiOperation("回复")
    public int reply(DiscussDTO DTO){
        DTO.setUserId( 1002L );
        return discussService.save( DTO.convert()  )  ? 1 : 0;
    }

    @GetMapping("/getQuestion")
    @ApiOperation(("获取问题信息"))
    public DiscussVO getQuestion( Long id ){
        Discuss question = discussService.getQuestionById(id);
        return new DiscussVO().from( question , DiscussVO.class );
    }


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
