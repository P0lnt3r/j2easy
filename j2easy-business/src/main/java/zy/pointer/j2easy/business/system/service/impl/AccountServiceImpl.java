package zy.pointer.j2easy.business.system.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.Transactional;
import sun.nio.cs.US_ASCII;
import zy.pointer.j2easy.business.system.entity.Account;
import zy.pointer.j2easy.business.system.mapper.AccountMapper;
import zy.pointer.j2easy.business.system.service.IAccountService;
import zy.pointer.j2easy.framework.business.AbsBusinessService;
import org.springframework.stereotype.Service;
import zy.pointer.j2easy.framework.log.annos.LogMethod;

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
    public int createAccount(String username, String password, Integer realm) {
        // 随机得出一个 8 字符长度的盐值.
        String salt = RandomUtil.randomNumbers(8);
        MD5 md5 = MD5.create();
        String md5_password = md5.digestHex( password );
        password = md5.digestHex( md5_password + salt  );
        Account account = new Account();
        account.setUsername( username );
        account.setPassword( password );
        account.setSalt( salt );
        return getBaseMapper().insert( account );
    }

    @Override
    public int login(String username, String password, Integer realm) {

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
