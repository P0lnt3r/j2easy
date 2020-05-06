package zy.pointer.j2easy.framework.business;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import zy.pointer.j2easy.framework.log.annos.LogMethod;
import zy.pointer.j2easy.framework.repository.BaseEntity;
import zy.pointer.j2easy.framework.repository.RepositoryMapper;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 通过继承 ServiceImpl 实现 IService 部分的 DB 操作.
 * @param <Mapper>
 * @param <Entity>
 */
public abstract class AbsBusinessService<
        Mapper extends RepositoryMapper<Entity>,        // Mapper extends RepositroyMapper ( extends BaseMapper[mybatis-plus] , Mapper[TopInterface] )
        Entity extends BaseEntity                       // 实体类
        >

        extends ServiceImpl< Mapper , Entity >          // mybatis-plus 抽象实现

        implements BusinessService<Entity>              // BaseService 继承了 mybatis-plus : IService 接口即顶级业务层接口

{

    @Override
    @LogMethod( name = "保存{}")
    public boolean save(Entity entity) {
        return super.save(entity);
    }

    @Override
    @LogMethod( name = "批量保存{}")
    public boolean saveBatch(Collection<Entity> entityList, int batchSize) {
        return super.saveBatch(entityList, batchSize);
    }

    @Override
    @LogMethod( name = "保存|更新{}" )
    public boolean saveOrUpdate(Entity entity) {
        return super.saveOrUpdate(entity);
    }

    @Override
    @LogMethod( name = "批量保存|更新")
    public boolean saveOrUpdateBatch(Collection<Entity> entityList, int batchSize) {
        return super.saveOrUpdateBatch(entityList, batchSize);
    }

    @Override
    @LogMethod( name = "通过ID删除{}")
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    @LogMethod( name = "通过Map删除{}")
    public boolean removeByMap(Map<String, Object> columnMap) {
        return super.removeByMap(columnMap);
    }

    @Override
    @LogMethod( name = "通过条件删除{}")
    public boolean remove(Wrapper<Entity> wrapper) {
        return super.remove(wrapper);
    }

    @Override
    @LogMethod( name = "通过ID集合删除{}")
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        return super.removeByIds(idList);
    }

    @Override
    @LogMethod( name = "通过ID更新{}")
    public boolean updateById(Entity entity) {
        return super.updateById(entity);
    }

    @Override
    @LogMethod( name = "通过条件更新{}")
    public boolean update(Entity entity, Wrapper<Entity> updateWrapper) {
        return super.update(entity, updateWrapper);
    }

    @Override
    @LogMethod( name = "批量通过ID更新{}")
    public boolean updateBatchById(Collection<Entity> entityList, int batchSize) {
        return super.updateBatchById(entityList, batchSize);
    }

    @Override
    @LogMethod( name = "通过ID查询{}")
    public Entity getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    @LogMethod( name = "通过ID集合查询{}")
    public Collection<Entity> listByIds(Collection<? extends Serializable> idList) {
        return super.listByIds(idList);
    }

    @Override
    @LogMethod( name = "通过条件MAP查询{}")
    public Collection<Entity> listByMap(Map<String, Object> columnMap) {
        return super.listByMap(columnMap);
    }

    @Override
    @LogMethod( name = "通过条件查询{}")
    public Entity getOne(Wrapper<Entity> queryWrapper, boolean throwEx) {
        return super.getOne(queryWrapper, throwEx);
    }

    @Override
    @LogMethod( name = "通过条件查询{}")
    public Map<String, Object> getMap(Wrapper<Entity> queryWrapper) {
        return super.getMap(queryWrapper);
    }

    @Override
    @LogMethod( name = "通过条件统计{}")
    public int count(Wrapper<Entity> queryWrapper) {
        return super.count(queryWrapper);
    }

    @Override
    @LogMethod( name = "通过条件查询{}")
    public List<Entity> list(Wrapper<Entity> queryWrapper) {
        return super.list(queryWrapper);
    }

    @Override
    @LogMethod( name = "通过条件查询并分页{}")
    public IPage<Entity> page(IPage<Entity> page, Wrapper<Entity> queryWrapper) {
        return super.page(page, queryWrapper);
    }

    @Override
    @LogMethod( name = "通过条件MAP查询")
    public List<Map<String, Object>> listMaps(Wrapper<Entity> queryWrapper) {
        return super.listMaps(queryWrapper);
    }

    @Override
    @LogMethod( name = "通过条件MAP查询 -> 并分页")
    public <V> List<V> listObjs(Wrapper<Entity> queryWrapper, Function<? super Object, V> mapper) {
        return super.listObjs(queryWrapper, mapper);
    }

    @Override
    @LogMethod( name = "通过条件MAP查询分页")
    public IPage<Map<String, Object>> pageMaps(IPage<Entity> page, Wrapper<Entity> queryWrapper) {
        return super.pageMaps(page, queryWrapper);
    }

    @Override
    @LogMethod( name = "通过条件查询")
    public <V> V getObj(Wrapper<Entity> queryWrapper, Function<? super Object, V> mapper) {
        return super.getObj(queryWrapper, mapper);
    }

    @Override
    public IPage<Entity> page(IPage<Entity> page) {
        return getBaseMapper().selectPage(page , null);
    }

    @Override
    @LogMethod( name = "通过条件MAP查询分页{}")
    public IPage<Entity> selectByMapForPage(Page<Entity> page, Map<String, Object> params) {
        if ( page.getOrders().isEmpty() ){
            page.addOrder( OrderItem.desc("_ID") );
        }
        return getBaseMapper().selectByMapForPage( page , params );
    }

}
