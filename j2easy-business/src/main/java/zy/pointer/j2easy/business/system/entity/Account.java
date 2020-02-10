package zy.pointer.j2easy.business.system.entity;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import springfox.documentation.annotations.ApiIgnore;
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
 * @since 2020-01-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="Account对象", description="")
@TableName("TB_SYSTEM_ACCOUNT")
public class Account extends BaseEntity{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "账户名")
    @TableField("USERNAME")
    private String username;

    @ApiModelProperty(value = "登陆密码")
    @TableField("PASSWORD")
    private String password;

    @ApiModelProperty(value = "账户状态[ 1:正常,2:锁定,3:禁止 ]")
    @TableField("STATE")
    private Integer state;

    @TableField("SALT")
    @ApiModelProperty(value = "密码加密盐值")
    private String salt;

    @ApiModelProperty(value = "解锁时间")
    @TableField("UNLOCK_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime unlockTime;

    @ApiModelProperty(value = "登陆重试次数")
    @TableField("RETRY_COUNT")
    private Integer retryCount;

    @ApiModelProperty(value = "所属域 [ 1:后台,2:前台,3:前后台通用 ]")
    @TableField("REALM")
    private Integer realm;


}
