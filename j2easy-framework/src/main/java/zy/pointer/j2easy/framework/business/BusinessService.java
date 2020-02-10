package zy.pointer.j2easy.framework.business;


import com.baomidou.mybatisplus.extension.service.IService;
import zy.pointer.j2easy.framework.repository.BaseEntity;
import zy.pointer.j2easy.framework.repository.ExtendsMapper;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * 基础业务接口
 *      继承 mybatis-plus IService 接口 , 即顶级业务接口
 * @param <Entity>
 */
public interface BusinessService<Entity extends BaseEntity> extends IService<Entity> , TopService , ExtendsMapper<Entity> {


}
