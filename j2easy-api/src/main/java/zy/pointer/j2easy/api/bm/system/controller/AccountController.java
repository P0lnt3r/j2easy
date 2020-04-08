package zy.pointer.j2easy.api.bm.system.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import zy.pointer.j2easy.api.bm.system.dto.AccountQueryDTO;
import zy.pointer.j2easy.api.bm.system.dto.AccountSaveDTO;
import zy.pointer.j2easy.api.bm.system.vo.AccountVO;
import zy.pointer.j2easy.business.system.entity.Account;
import zy.pointer.j2easy.business.system.service.IAccountService;
import zy.pointer.j2easy.framework.web.model.vo.PageVo;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhouyang
 * @since 2020-01-15
 */
@RestController
@RequestMapping("/api/bm/system/account")
@Api( "账户管理" )
public class AccountController {

    @Autowired
    IAccountService accountService;

    @GetMapping("/checkUsernameExists")
    @ApiOperation("检查账户用户名是否存在")
    @ApiImplicitParams({
            @ApiImplicitParam( paramType="query", name="username", dataType="String", required=true, value="账户名" ),
    })
    public int checkUsernameExists( @NotEmpty String username ){
        return accountService.checkUsernameExists(username);
    }

    @PostMapping( value = "/saveOrUpdate")
    @ApiOperation("保存更新Account信息")
    @RequiresPermissions( value = "/backed/system/account:saveOrUpdate" )
    public AccountVO saveOrUpdate(@Valid AccountSaveDTO dto ){
        Account account = dto.convert();
        accountService.saveOrUpdate( account );
        return new AccountVO().from( account , AccountVO.class );
    }

    @GetMapping( value = "/query" )
    @ApiOperation( "获取Account信息" )
    public PageVo<AccountVO,Account> query( @Valid AccountQueryDTO dto){
        IPage<Account> page = accountService.selectByMapForPage( dto.convert() , BeanUtil.beanToMap( dto ));
        return new PageVo<AccountVO , Account>().from( page ,AccountVO.class );
    }

}
