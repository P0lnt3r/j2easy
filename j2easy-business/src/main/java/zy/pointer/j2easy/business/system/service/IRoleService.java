package zy.pointer.j2easy.business.system.service;

import zy.pointer.j2easy.business.system.entity.Permission;
import zy.pointer.j2easy.business.system.entity.Role;
import zy.pointer.j2easy.framework.business.BusinessService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhouyang
 * @since 2020-04-13
 */
public interface IRoleService extends BusinessService<Role> {

    Role getRoleWithPermission( Long id );

    int assignPermission( Long id , Long[] permissionIds );

}
