package zy.pointer.j2easy.business.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import zy.pointer.j2easy.framework.repository.BaseEntity;

import java.util.List;
import java.util.Objects;

@Data
@ApiModel( "权限信息" )
@TableName( "TB_SYSTEM_PERMISSION" )
public class Permission extends BaseEntity {

    @ApiModelProperty( "父节点ID" )
    @TableField( "P_ID" )
    Long pId;

    @ApiModelProperty( "权限名称" )
    @TableField( "NAME" )
    private String name;

    @ApiModelProperty( "权限值" )
    @TableField( "VAL" )
    private String value;

    @ApiModelProperty( "权限类型" )
    @TableField( "TYPE" )
    private Integer type;

    @ApiModelProperty( "权限路径" )
    @TableField( "PATH" )
    private String path;

    @ApiModelProperty( "权限状态" )
    @TableField( "STATE" )
    private Integer state;

    @ApiModelProperty( "权限级别" )
    @TableField( "LEVEL" )
    private Integer level;

    @TableField( exist = false )
    private Permission parent;

    @TableField( exist = false )
    private List<Permission> childrenList;

    @Override
    public boolean equals(Object obj) {
        if ( obj instanceof Permission ){
            boolean result = value != null && value.equals( ((Permission) obj) .getValue() );
            return result;
        }
        return  super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
