package zy.pointer.j2easy.business.cms.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import zy.pointer.j2easy.framework.repository.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * <p>
 * 
 * </p>
 *
 * @author zhouyang
 * @since 2020-04-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="Course对象", description="")
@TableName("TB_CMS_COURSE")
public class Course extends BaseEntity{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程名称")
    @TableField("NAME")
    private String name;

    @TableField( exist = false)
    @ApiModelProperty( "提问数量" )
    private Integer questions;

    @TableField( exist = false)
    @ApiModelProperty( "回答数量" )
    private Integer replies;

}
