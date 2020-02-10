package zy.pointer.j2easy.framework.web.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import zy.pointer.j2easy.framework.commons.reflect.ReflectUtil;
import zy.pointer.j2easy.framework.repository.BaseEntity;

import javax.validation.constraints.*;
import java.lang.reflect.Field;

@Data
public abstract class PageQueryDTO< Entity extends BaseEntity > {

    @ApiModelProperty( "查询第几页数据" )
    @NotNull
    @Min( value = 1 )
    private Long page;

    @NotNull
    @Min( value = 1 )
    @ApiModelProperty( "每页多少条数据" )
    private Long size;

    @ApiModelProperty( "排序属性" )
    private String orderProp;

    @ApiModelProperty( "排序方式[asc:正序,desc:倒序]" )
    private String orderMode;

    /**
     * 子类可以重写这个方法来定义 prop -> column 的值,从而构建 IPage 对象
     * @param orderProp
     * @return
     */
    public String getColumn( String orderProp ){
        return null;
    }

    public Page<Entity> convert() {
        Page<Entity> page = new Page<>();
        page.setCurrent( this.page );
        page.setSize(    this.size );
        if ( orderProp != null && !"".equals(orderProp.trim()) ){
            String column = getColumn( this.orderProp );
            if ( column == null ){
                try {
                    Class clazz = ReflectUtil.getGenericsClass( this.getClass() , 0 );
                    Field field = clazz.getDeclaredField( orderProp );
                    TableField tableField = null;
                    if ( (tableField = field.getDeclaredAnnotation( TableField.class )) != null ){
                        String columnName = tableField.value(); // 属性对应的列名
                        if ( !"asc".equals( orderMode ) && !"desc".equals(orderMode) ){
                            orderMode = "desc";
                        }
                        OrderItem orderItem = new OrderItem();
                        orderItem.setAsc( "asc".equals( orderMode ) );
                        orderItem.setColumn(columnName);
                        page.addOrder(orderItem);
                    }
                }catch ( Exception e ){
                    column = null;
                }
            }
        }
        return page;
    }



}
