package zy.pointer.j2easy.framework.web.model.vo;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import zy.pointer.j2easy.framework.repository.BaseEntity;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PageVo<ValueObject extends zy.pointer.j2easy.framework.web.model.vo.ValueObject<Entity> , Entity extends BaseEntity> {

    @ApiModelProperty( "当前第几页" )
    private Long page;

    @ApiModelProperty( "每页多少条数据" )
    private Long size;

    @ApiModelProperty( "合计多少条数据" )
    private Long total;

    @ApiModelProperty( "一共多少页数据" )
    private Long totalPages;

    @ApiModelProperty( "数据列表" )
    private List<ValueObject> records;

    public  PageVo<ValueObject,Entity> from( IPage<Entity> page , Class<ValueObject> voClass ) {
        return from( page , voClass , null );
    }

    public  PageVo<ValueObject,Entity> from(IPage<Entity> page , Class<ValueObject> voClass , IConvertHandler<Entity , ValueObject> handler ){
        this.page = page.getCurrent();
        this.size = page.getSize();
        this.total = page.getTotal();
        this.totalPages = page.getPages();
        try {
            Constructor<ValueObject> constructor = voClass.getConstructor();
            ValueObject vo = constructor.newInstance();
            List<ValueObject> records = page.getRecords().stream().map( entity -> {
                if ( handler != null ){
                    return handler.handle( entity , vo );
                }
                return (ValueObject) vo.from( entity , vo.getClass() );
            } ).collect(Collectors.toList());
            this.records = records;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }


}
