package zy.pointer.j2easy.business.system.service;

import zy.pointer.j2easy.business.system.entity.Permission;
import zy.pointer.j2easy.framework.business.BusinessService;

import java.util.List;

public interface IPermissionService extends BusinessService<Permission> {

    Permission buildFuncTypePermission( String path , String name );

    Permission buildMenuTypePermission( String path , String name );

    List<Permission> getRealms();

    /**
     * 获取 ROOT - Permission
     * @return
     */
    Permission getRoot();

    /**
     * 初始化 / , /bm , /public  , /apps 四个根级别的权限菜单
     */
    void initRealmsPermission();

    /**
     * 获取所有的菜单型权限
     * @return
     */
    List<Permission> getAllMenuPermissions();

    /**
     * 获取所有的功能型权限
     * @return
     */
    List<Permission> getAllFuncPermissions();

}
