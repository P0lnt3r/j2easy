package zy.pointer.j2easy.business.system.service;

import zy.pointer.j2easy.business.system.entity.Account;
import zy.pointer.j2easy.business.system.entity.Role;
import zy.pointer.j2easy.framework.business.BusinessService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhouyang
 * @since 2020-01-15
 */
public interface IAccountService extends BusinessService<Account> {

    Account login( String username , String password , Integer realm );

    /**
     * 通过账户名从账户表中获取账户数据
     * @param username
     * @return
     */
    Account findByUsername( String username );

    int checkUsernameExists( String username );

    List<Role> getRoleList( Long id );

    int assignRole( Long id , Long[] roleIds );

}
