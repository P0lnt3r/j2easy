package zy.pointer.j2easy.business.system.service;

import zy.pointer.j2easy.business.system.entity.Permission;
import zy.pointer.j2easy.framework.business.BusinessService;

import java.util.List;

public interface IPermissionService extends BusinessService<Permission> {

    /**
     * 获取 ROOT - Permission
     * @return
     */
    Permission getRoot();

    /**
     * 对 2级 以上的权限进行 数据包装
     * @param value
     * @param name
     * @return
     */
    Permission wrapByValue( String value , String name );

    /**
     * 将 permission 加入到 root - Permission 中，
     * @param permission
     */
    void add( Permission permission );

    /**
     * 将 permission 加入到 root-Permission 中,并同时存储到数据库中.
     * @param permission
     */
    void addNew( Permission permission );

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
