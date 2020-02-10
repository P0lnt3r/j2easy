package zy.pointer.j2easy.business.system.service.impl;

import org.springframework.context.annotation.Primary;
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
public class AccountServiceImpl extends AbsBusinessService<AccountMapper, Account> implements IAccountService {

}
