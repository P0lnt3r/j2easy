package zy.pointer.j2easy.business.system.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import zy.pointer.j2easy.business.system.entity.Role;
import zy.pointer.j2easy.framework.repository.RepositoryMapper;

import java.util.List;

@Repository
public interface RoleMapper extends RepositoryMapper<Role> {

    /**
     * 为角色加载权限
     * @param id 角色ID
     * @return
     */
    Role selectRoleWithPermission( @Param("id") Long id );

    List<Role> selectAccountRoleList( @Param("accountId") Long accountId );

    void clearPermission( @Param("id") Long id );

    int assignPermission( @Param("id") Long id , @Param("permissionIds") Long[] permissionIds );

}
