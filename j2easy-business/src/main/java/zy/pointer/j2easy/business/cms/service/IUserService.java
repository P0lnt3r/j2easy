package zy.pointer.j2easy.business.cms.service;

import zy.pointer.j2easy.business.cms.entity.User;
import zy.pointer.j2easy.framework.business.BusinessService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhouyang
 * @since 2020-04-15
 */
public interface IUserService extends BusinessService<User> {

    User findByUserId( Long userId );

}
