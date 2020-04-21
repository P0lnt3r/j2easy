package zy.pointer.j2easy.business.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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

    @Override
    public User findByUserId(Long userId) {
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq( User::getUserId , userId );
        return getBaseMapper().selectOne( queryWrapper );
    }
}
