package zy.pointer.j2easy.business.system.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.Transactional;
import sun.nio.cs.US_ASCII;
import zy.pointer.j2easy.business.commons.ErrorCode;
import zy.pointer.j2easy.business.system.entity.Account;
import zy.pointer.j2easy.business.system.entity.Role;
import zy.pointer.j2easy.business.system.mapper.AccountMapper;
import zy.pointer.j2easy.business.system.mapper.RoleMapper;
import zy.pointer.j2easy.business.system.service.IAccountService;
import zy.pointer.j2easy.business.system.service.IRoleService;
import zy.pointer.j2easy.framework.business.AbsBusinessService;
import org.springframework.stereotype.Service;
import zy.pointer.j2easy.framework.exception.BusinessException;
import zy.pointer.j2easy.framework.log.annos.LogMethod;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhouyang
 * @since 2020-01-15
 */
@Service
@Primary
@Transactional
public class AccountServiceImpl extends AbsBusinessService<AccountMapper, Account> implements IAccountService {

    @Autowired
    RoleMapper roleMapper;

    @Override
    @Transactional( readOnly = true )
    public Account findByUsername(String username) {
        LambdaQueryWrapper<Account> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq( Account::getUsername , username );
        return getBaseMapper().selectOne( queryWrapper );
    }

    @Override
    public int checkUsernameExists(String username) {
        return findByUsername(username) == null ? 0 : 1;
    }

    @Override
    public boolean saveOrUpdate(Account entity) {
        String password = entity.getPassword();
        if ( entity.getId() == null ){
            String salt = RandomUtil.randomNumbers(8);
            password = md5Password(password , salt);
            entity.setPassword(password);
            entity.setSalt(salt);
        }else{
            Account _account = findByUsername( entity.getUsername() );
            String salt = _account.getSalt();
            password = md5Password(password , salt);
            entity.setPassword(password);
        }
        return super.saveOrUpdate(entity);
    }

    @Override
    public Account login(String username, String password, Integer realm) {
        LambdaQueryWrapper<Account> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq( Account::getUsername , username );
        Account account = getBaseMapper().selectOne( queryWrapper );
        // TODO : 针对用户的状态以及相关的一些设置,可以具体做一些逻辑
        if ( account == null ){
            throw new BusinessException(ErrorCode.ERROR_CODE_1006);
        }
        String _password = account.getPassword();
        if ( ! _password.equals( md5Password( password , account.getSalt() ) ) ){
            throw new BusinessException(ErrorCode.ERROR_CODE_1006);
        }
        return account;
    }

    private String md5Password( String password , String salt ){
        MD5 md5 = MD5.create();
        return md5.digestHex( md5.digestHex(password) + salt );
    }

    @Override
    public List<Role> getRoleList(Long id) {
        return roleMapper.selectAccountRoleList( id );
    }

    @Override
    public int assignRole(Long id, Long[] roleIds) {
        // 清掉账户已经分配的角色.
        getBaseMapper().clearRole( id );
        // 重新装上新的角色
        getBaseMapper().assignRole( id , roleIds );
        return 0;
    }

    public static void main(String[] args) {
       String salt = RandomUtil.randomNumbers(8);
       String password = "123456";
       MD5 md5 = MD5.create();
       String result = md5.digestHex( md5.digestHex(password) + salt );
       System.out.println(result);
    }

}
