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
            "/api" ,        // root
            "/api/bm",      // 后台管理接口
            "/api/apps",    // 应用服务接口
            "/api/public"   // 公共接口服务
    };

    private Permission root;

    @Override
    public Permission getRoot() {
        return root;
    }

    /**
     * 将 Value 包装为 Permission , 主要针对 2级 级别菜单封装
     * @param value
     * @param name
     * @return
     */
    @Override
    public Permission wrapByValue(String value , String name) {
        Permission permission = new Permission();
        permission.setValue( value );
        permission.setName( name );
        String path = "/" + value.replaceAll("\\." , "/" );
        permission.setPath( path );
        int level = path.split("/").length;
        if ( value.contains(":") ){
            permission.setLevel( level );
            permission.setPath( path.replaceAll(":" , "/") );
            permission.setType( Convert.toInt( DictConstant.SYSTEM_PERMISSION_TYPE_FUNC.value )  );
        }else{
            permission.setLevel( level - 1);
            permission.setType( Convert.toInt( DictConstant.SYSTEM_PERMISSION_TYPE_MENU.value ) );
        }
        permission.setState( Convert.toInt( DictConstant.SYSTEM_PERMISSION_STATE_ACTIVE.value ) );
        return permission;
    }

    @Override
    @LogMethod( name = "初始化权限Realms" , logOut = false )
    public void initRealmsPermission() {
        LambdaQueryWrapper<Permission> wrapper = Wrappers.lambdaQuery( new Permission() )
                .in( Permission::getPath , DEFAULT_PERMISSION_MENU );
        List<Permission> permissionList = list(wrapper);
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
        if (  unBuildPaths.contains("/bm") ){
            realmPermissionList.add (saveRealm( "bm" , "/bm" , "后台管理" , root.getId() ));
        }else{
            realmPermissionList.add(permissionList.stream().filter( permission -> "/bm".equals( permission.getPath() ) ).findFirst().get());
        }
        if (  unBuildPaths.contains("/apps") ){
            realmPermissionList.add (saveRealm( "apps" , "/apps" , "应用服务" , root.getId() ));
        }else{
            realmPermissionList.add(permissionList.stream().filter( permission -> "/apps".equals( permission.getPath() ) ).findFirst().get());
        }
        if (  unBuildPaths.contains("/public") ){
            realmPermissionList.add (saveRealm( "public" , "/public" , "公共服务" , root.getId() ) );
        }else {
            realmPermissionList.add(permissionList.stream().filter( permission -> "/public".equals( permission.getPath() ) ).findFirst().get());
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
                .gt( Permission::getLevel , 1 )
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

    public void add(Permission permission ){
        String path = permission.getPath();
        String parentPath = path.substring( 0 ,path.lastIndexOf("/") );
        Permission parent = find(parentPath);
        if ( parent.getChildrenList() == null ){
            parent.setChildrenList( new ArrayList<>() );
        }
        parent.getChildrenList().add( permission );
    }

    public void addNew( Permission permission ){
        String path = permission.getPath();
        String parentPath = path.substring( 0 ,path.lastIndexOf("/") );
        Permission parent = find(parentPath);
        permission.setPId( parent.getId() );
        save(permission);
        if ( parent.getChildrenList() == null ){
            parent.setChildrenList( new ArrayList<>() );
        }
        parent.getChildrenList().add( permission );
    }

    /**
     *
     * @return
     */
    public Permission find( String path ){
        // /bm/system/asset/Account
        /*
         *  将其拆分为如下数组结构,一层一层得向下解析寻找
            /
         *  /bm
         *  /bm/system
         *  /bm/system/Account
         */
        String[] arr = path.split("/");
        String[] target = new String[ arr.length ];
        for( int i = 0; i<arr.length; i++ ){
            String _value = "";
            for( int j = 0;j<i;j++ ){
                _value = _value + arr[j] + "/";
            }
            target[i] = _value + arr[i];
        }
        target[0] = "/";
        Permission result = null;
        for( int i = 0;i<target.length;i++ ){
            /*
                 第一次调用 : root , / => root
                 第二次调用 : root , /bm => bm
                 第三次调用 : bm   , /bm/system => system
                 第四次调用 : system,/bm/system/Account => Account
             */
            result = find( result == null ? root : result , target[i] );
        }
        return result;
    }

    public Permission find( Permission cursor , String path ){
        if ( cursor.getPath().equals( path ) ){
            return cursor;
        }
        return cursor.getChildrenList()
                .stream()
                .filter( perm -> path.equals( perm.getPath() ) )
                .findFirst().get();
    }


}
