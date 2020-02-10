package zy.pointer.j2easy.framework.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface RepositoryMapper<Entity extends BaseEntity> extends BaseMapper<Entity> , ExtendsMapper<Entity> , TopMapper {



}
