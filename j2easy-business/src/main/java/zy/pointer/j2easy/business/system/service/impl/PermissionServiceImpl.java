package zy.pointer.j2easy.business.system.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zy.pointer.j2easy.business.commons.DictConstant;
import zy.pointer.j2easy.business.system.entity.Permission;
import zy.pointer.j2easy.business.system.mapper.PermissionMapper;
import zy.pointer.j2easy.business.system.service.IPermissionService;
import zy.pointer.j2easy.framework.business.AbsBusinessService;
import zy.pointer.j2easy.framework.log.annos.LogMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@Primary
public class PermissionServiceImpl extends AbsBusinessService<PermissionMapper , Permission> implements IPermissionService {

    public static final String[] DEFAULT_PERMISSION_MENU = {
            "/" ,           // root
            "/api/bm",      // 后台管理接口
            "/api/apps",    // 应用服务接口
            "/api/public"   // 公共接口服务
    };

    private Permission root;

    @Override
    public Permission buildFuncTypePermission(String path, String name) {

        Permission permission = new Permission();
        permission.setPath( path );
        /*
         * path : /api/bm/system/account/query
         * value: api.bm.system.account:query
         */
        String[] tempArr = path.substring(0).split("/");
        String value = "";
        for (int i = 1; i < tempArr.length; i++) {
            if ( i == tempArr.length - 1 ){
                value = value.substring(0,value.length()-1) + ":" + tempArr[i] ;
                break;
            }
            value = value + tempArr[i] + "." ;
        }
        permission.setValue( value );
        permission.setName ( name );
        permission.setLevel( path.split("/").length );
        permission.setType ( Convert.toInt( DictConstant.SYSTEM_PERMISSION_TYPE_FUNC.value )  );
        permission.setState( Convert.toInt( DictConstant.SYSTEM_PERMISSION_STATE_ACTIVE.value ) );
        return permission;
    }

    @Override
    public Permission buildMenuTypePermission(String path, String name) {
        Permission permission = new Permission();
        permission.setPath( path );
        /*
         * path : /api/bm/system/account
         * value: api.bm.system.account
         */
        String value = path.substring(1).replaceAll( "/" , "\\." );
        permission.setValue( value);
        permission.setName (  name);
        permission.setLevel( path.split("/").length );
        permission.setType (  Convert.toInt( DictConstant.SYSTEM_PERMISSION_TYPE_MENU.value )  );
        permission.setState( Convert.toInt( DictConstant.SYSTEM_PERMISSION_STATE_ACTIVE.value ) );
        return permission;
    }

    @Override
    public Permission getRoot() {
        return root;
    }

    @Override
    @LogMethod( name = "初始化权限Realms" , logOut = false )
    public void initRealmsPermission() {

        LambdaQueryWrapper<Permission> wrapper = Wrappers.lambdaQuery( new Permission() )
                .in( Permission::getPath , DEFAULT_PERMISSION_MENU );
        List<Permission> permissionList = list(wrapper);

        // 查询
        Set<String> dbPaths = permissionList.stream().map( Permission::getPath ).collect(Collectors.toSet());
        Set<String> paths = Stream.of( DEFAULT_PERMISSION_MENU ).collect(Collectors.toSet());
        Set<String> unBuildPaths = paths.stream().filter( path -> !dbPaths.contains(path) ).collect(Collectors.toSet());

        if ( unBuildPaths.contains("/") ){
            // 构建 ROOT - Permission
            root = new Permission();
            root.setValue("/");
            root.setPath("/");
            root.setName("root");
            root.setType(Convert.toInt( DictConstant.SYSTEM_PERMISSION_TYPE_MENU.value ) );
            root.setState( Convert.toInt( DictConstant.SYSTEM_PERMISSION_STATE_ACTIVE.value ) );
            root.setLevel(0);
            save( root );
        }else{
            root = permissionList.stream().filter( permission -> permission.getPath().equals("/") ).findFirst().get();
        }
        List<Permission> realmPermissionList = new ArrayList<>();
        if (  unBuildPaths.contains("/api/bm") ){
            realmPermissionList.add (saveRealm( "api.bm" , "/api/bm" , "后台管理" , root.getId() ));
        }else{
            realmPermissionList.add(permissionList.stream().filter( permission -> "/api/bm".equals( permission.getPath() ) ).findFirst().get());
        }
        if (  unBuildPaths.contains("/api/apps") ){
            realmPermissionList.add (saveRealm( "api.apps" , "/api/apps" , "应用服务" , root.getId() ));
        }else{
            realmPermissionList.add(permissionList.stream().filter( permission -> "/api/apps".equals( permission.getPath() ) ).findFirst().get());
        }
        if (  unBuildPaths.contains("/api/public") ){
            realmPermissionList.add (saveRealm( "api.public" , "/api/public" , "公共服务" , root.getId() ) );
        }else {
            realmPermissionList.add(permissionList.stream().filter( permission -> "/api/public".equals( permission.getPath() ) ).findFirst().get());
        }
        root.setChildrenList( realmPermissionList );
    }

    /**
     * 专为 /bm  ,  /apps , / public 三个包 进行权限封装录入.
     * @param value
     * @param path
     * @param name
     * @param rootPermissionId
     * @return
     */
    private Permission saveRealm( String value , String path , String name , Long rootPermissionId ){
        Permission permission = new Permission();
        permission.setValue(value);
        permission.setPath(path);
        permission.setName(name);
        permission.setPId( rootPermissionId );
        permission.setType(Convert.toInt( DictConstant.SYSTEM_PERMISSION_TYPE_MENU.value ) );
        permission.setState( Convert.toInt( DictConstant.SYSTEM_PERMISSION_STATE_ACTIVE.value ) );
        permission.setLevel(1);
        save(permission);
        return permission;
    }

    @Override
    @LogMethod( name = "查询所有的菜单型权限")
    public List<Permission> getAllMenuPermissions() {
        LambdaQueryWrapper<Permission> wrapper = Wrappers.lambdaQuery( new Permission() )
                .eq( Permission::getType , DictConstant.SYSTEM_PERMISSION_TYPE_MENU.value() )
                .orderByAsc( Permission::getLevel );    // 通过 LEVEL 进行正序排序
        return list(wrapper);
    }

    @Override
    @LogMethod( name = "查询所有的功能型权限" , logOut = false)
    public List<Permission> getAllFuncPermissions() {
        LambdaQueryWrapper<Permission> wrapper = Wrappers.lambdaQuery( new Permission() )
                .eq( Permission::getType , DictConstant.SYSTEM_PERMISSION_TYPE_FUNC.value() )
                .orderByAsc( Permission::getLevel );
        return list(wrapper);
    }

}
