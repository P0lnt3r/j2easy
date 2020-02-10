package zy.pointer.j2easy.framework.repository.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 对 BaseEntity 中的 数据创建时间,更新时间,Trace 等值进行自动填充
 */
@Component
public class BaseEntityFieldAutoFillHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        this.setFieldValByName( "createTime" , now , metaObject  );
        this.setFieldValByName( "dbState" , 1 , metaObject );
        this.setFieldValByName( "updateTime" , now , metaObject );
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        this.setFieldValByName( "updateTime" , now , metaObject );
    }
}
