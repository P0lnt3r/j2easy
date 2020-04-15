package zy.pointer.j2easy.business.cms.service.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.Transactional;
import zy.pointer.j2easy.business.cms.entity.User;
import zy.pointer.j2easy.business.cms.mapper.UserMapper;
import zy.pointer.j2easy.business.cms.service.IUserService;
import zy.pointer.j2easy.framework.business.AbsBusinessService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhouyang
 * @since 2020-04-15
 */
@Service
@Primary
@Transactional
public class UserServiceImpl extends AbsBusinessService<UserMapper, User> implements IUserService {

}
