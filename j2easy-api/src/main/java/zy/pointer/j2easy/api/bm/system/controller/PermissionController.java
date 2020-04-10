package zy.pointer.j2easy.api.bm.system.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zy.pointer.j2easy.api.bm.system.dto.PermissionDTO;
import zy.pointer.j2easy.api.bm.system.dto.PermissionQueryDTO;
import zy.pointer.j2easy.api.bm.system.vo.PermissionVO;
import zy.pointer.j2easy.api.bm.system.vo.TreeNodePermissionVO;
import zy.pointer.j2easy.business.system.entity.Permission;
import zy.pointer.j2easy.business.system.service.IPermissionService;
import zy.pointer.j2easy.framework.datastructuers.pathtree.PathTree;
import zy.pointer.j2easy.framework.datastructuers.pathtree.PathTreeNode;
import zy.pointer.j2easy.framework.web.model.vo.PageVo;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping( "/api/bm/system/permission" )
@Api( "菜单权限" )
public class PermissionController {

    @Autowired
    IPermissionService permissionService;

    @GetMapping("/realms")
    @ApiOperation("获取域")
    public List<PermissionVO> realms(){
        return permissionService.getRealms().stream().map( permission -> {
            PermissionVO vo = new PermissionVO();
            vo.from( permission , PermissionVO.class );
            return vo;
        } ).collect( Collectors.toList() );
    }

    @GetMapping("/catalog")
    @ApiOperation("获取菜单权限树")
    @ApiImplicitParams({
            @ApiImplicitParam( paramType="query", name="prefix", dataType="String", required=true, value="/api/bm" ),
    })
    public List<TreeNodePermissionVO> catalog(final String prefix ) throws InstantiationException, IllegalAccessException {
        List<Permission> permissionList = permissionService.getAllMenuPermissions().stream()
                .filter(permission -> permission.getPath() != null && permission.getPath().startsWith(prefix) )
                .collect( Collectors.toList() );
        PathTree<Permission> tree = new PathTree<Permission>();
        permissionList.forEach( permission -> tree.put( new PathTreeNode( permission.getPath() , permission.getName() , permission )) );
        if ( tree.get( prefix ) != null ){
            TreeNodePermissionVO root = tree.get(prefix).convert( TreeNodePermissionVO.class , (pathTreeNode , vo ) -> {
                Permission permission = (Permission)pathTreeNode.getPayload();
                vo.setKey( permission.getValue() );
                vo.setTitle(permission.getName());
                vo.setPayload( new PermissionVO().from( permission , PermissionVO.class ) );
                return vo;
            } );
            return root.getChildren();
        }
        return new ArrayList<>();
    }

    @GetMapping("/query")
    @ApiOperation("分页查询")
    public PageVo<PermissionVO , Permission> query( @Valid PermissionQueryDTO DTO ){
        IPage<Permission> iPage = permissionService.selectByMapForPage( DTO.convert()  , BeanUtil.beanToMap( DTO ));
        return new PageVo().from( iPage , PermissionVO.class );
    }

    @PostMapping("/update")
    @ApiOperation("权限更新")
    public int update(@Valid PermissionDTO DTO){
        Permission permission = DTO.convert();
        return permissionService.updateById( permission ) ? 1 : 0;
    }


}
