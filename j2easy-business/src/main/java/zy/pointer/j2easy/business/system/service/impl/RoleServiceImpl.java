package zy.pointer.j2easy.business.system.service.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.Transactional;
import zy.pointer.j2easy.business.system.entity.Role;
import zy.pointer.j2easy.business.system.mapper.RoleMapper;
import zy.pointer.j2easy.business.system.service.IRoleService;
import zy.pointer.j2easy.framework.business.AbsBusinessService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhouyang
 * @since 2020-04-13
 */
@Service
@Primary
@Transactional
public class RoleServiceImpl extends AbsBusinessService<RoleMapper, Role> implements IRoleService {

    @Override
    public Role getRoleWithPermission(Long id) {
        return getBaseMapper().selectRoleWithPermission(id);
    }

    @Override
    public int assignPermission(Long id, Long[] permissionIds) {
        getBaseMapper().clearPermission(id);
        getBaseMapper().assignPermission(id , permissionIds);
        return 1;
    }
}
