package zy.pointer.j2easy.business.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import zy.pointer.j2easy.framework.repository.BaseEntity;

/**
 *
 */
@Data
@TableName( "TB_SYSTEM_DICT" )
@ApiModel( value = "字典数据")
public class Dict extends BaseEntity {

    @TableField( value = "P_ID")
    @ApiModelProperty( value = "上级字典节点ID")
    private Long pId;

    /**
     * 字典名称
     */
    @ApiModelProperty( "字典节点名称" )
    @TableField( value = "NAME")
    private String name;

    /**
     * 字典类型:字典值 | 字典目录
     */
    @TableField( value = "TYPE" )
    @ApiModelProperty( "字典类型:[1:字典值 | 2:字典目录]" )
    private Integer type;

    @TableField( value = "PATH")
    @ApiModelProperty( "" )
    private String path;

    /**
     * 字典全局唯一标识值
     */
    @TableField( value = "UNIQ")
    @ApiModelProperty( value = "全局唯一标识")
    private String uniq;

    @TableField( value = "VAL")
    @ApiModelProperty( "字典值" )
    private String val;

}
