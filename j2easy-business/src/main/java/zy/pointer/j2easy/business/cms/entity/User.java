package zy.pointer.j2easy.business.cms.entity;
import java.time.LocalDateTime;
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
@ApiModel(value="User对象", description="")
@TableName( "TB_CMS_USER" )
public class User extends BaseEntity{

    private static final long serialVersionUID = 1L;

    @TableField("USER_ID")
    private String userId;

    @TableField("NAME")
    private String name;

    @ApiModelProperty(value = "用户类型[ 1:学生,2:老师 ]")
    @TableField("TYPE")
    private Integer type;

    @ApiModelProperty(value = "用户注册时间")
    @TableField("REGIST_TIME")
    private LocalDateTime registTime;

}
