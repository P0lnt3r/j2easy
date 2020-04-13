package zy.pointer.j2easy.api.bm.system.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zy.pointer.j2easy.api.bm.system.dto.PermissionQueryDTO;
import zy.pointer.j2easy.api.bm.system.dto.RoleDTO;
import zy.pointer.j2easy.api.bm.system.dto.RoleQueryDTO;
import zy.pointer.j2easy.api.bm.system.vo.PermissionVO;
import zy.pointer.j2easy.api.bm.system.vo.RoleVO;
import zy.pointer.j2easy.business.system.entity.Permission;
import zy.pointer.j2easy.business.system.entity.Role;
import zy.pointer.j2easy.business.system.service.IRoleService;
import zy.pointer.j2easy.framework.web.model.vo.PageVo;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/bm/system/role")
@Api("角色管理")
public class RoleController {

    @Autowired
    IRoleService roleService;

    @GetMapping("/query")
    @ApiOperation("分页查询")
    public PageVo<PermissionVO, Permission> query(@Valid RoleQueryDTO DTO ){
        IPage<Role> iPage = roleService.selectByMapForPage( DTO.convert()  , BeanUtil.beanToMap( DTO ));
        return new PageVo().from( iPage , RoleVO.class );
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存更新")
    public int saveOrUpdate(@Valid RoleDTO DTO){
        Role role = DTO.convert();
        return roleService.saveOrUpdate(role) ? 1 : 0;
    }

}
