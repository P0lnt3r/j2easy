package zy.pointer.j2easy.framework.web.model.dto;


import cn.hutool.core.bean.BeanUtil;
import zy.pointer.j2easy.framework.commons.reflect.ReflectUtil;
import zy.pointer.j2easy.framework.repository.BaseEntity;


public abstract class AbsDataTransferObject<Entity extends BaseEntity> implements DataTransferObject<Entity>  {

    @Override
    public Entity convert() {
        Entity entity = null;
        try {
            entity = (Entity) ReflectUtil.getGenericsClass( this.getClass() , 0 ).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        BeanUtil.copyProperties( this , entity );
        return entity;
    }

    @Override
    public <DataTransferObject> Entity convert( Class<DataTransferObject> clazz , IConvertHandler<DataTransferObject,Entity> handler) {
        if ( handler != null ){
            try {
                Entity entity = (Entity) ReflectUtil.getGenericsClass( this.getClass() , 0 ).newInstance();
                return handler.handle( (DataTransferObject)this , entity );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return convert();
    }
}
