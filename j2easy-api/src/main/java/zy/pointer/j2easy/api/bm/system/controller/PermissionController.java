package zy.pointer.j2easy.api.bm.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zy.pointer.j2easy.api.bm.system.vo.TreePermissionVo;
import zy.pointer.j2easy.business.system.entity.Permission;
import zy.pointer.j2easy.business.system.service.IPermissionService;
import zy.pointer.j2easy.framework.datastructuers.pathtree.PathTree;
import zy.pointer.j2easy.framework.datastructuers.pathtree.PathTreeNode;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping( "/api/bm/system/permission" )
@Api( "菜单权限" )
public class PermissionController {

    @Autowired
    IPermissionService permissionService;

    @GetMapping("/catalog")
    @ApiOperation("获取菜单权限树")
    public List<TreePermissionVo> catalog( ) throws InstantiationException, IllegalAccessException {
        final String prefix = "/api/bm";
        List<Permission> permissionList = permissionService.getAllMenuPermissions().stream()
                .filter(permission -> permission.getPath().startsWith(prefix) )
                .collect( Collectors.toList() );
        PathTree<Permission> tree = new PathTree<Permission>();
        permissionList.forEach( permission -> tree.put( new PathTreeNode( permission.getPath() , permission.getName() , permission )) );

        TreePermissionVo root = tree.get("/api/bm").convert( TreePermissionVo.class , ( pathTreeNode , vo ) -> {
            Permission permission = (Permission)pathTreeNode.getPayload();
            vo.setId( permission.getId() );
            vo.setKey( permission.getValue() );
            vo.setTitle(permission.getName());
            return vo;
        } );
        return root.getChildren();
    }

}
