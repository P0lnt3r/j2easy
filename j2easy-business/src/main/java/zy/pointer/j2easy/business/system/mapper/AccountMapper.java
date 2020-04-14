package zy.pointer.j2easy.business.system.mapper;

import org.apache.ibatis.annotations.Param;
import zy.pointer.j2easy.business.system.entity.Account;
import zy.pointer.j2easy.framework.repository.RepositoryMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhouyang
 * @since 2020-01-15
 */
public interface AccountMapper extends RepositoryMapper<Account> {

    int clearRole(@Param("id") Long id);

    int assignRole( @Param("id") Long id , @Param("roleIds") Long[] roleIds );

}
