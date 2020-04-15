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
@ApiModel(value="Discuss对象", description="")
@TableName("TB_CMS_DISCUSS")
public class Discuss extends BaseEntity{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "类型:{ 1:提问,2:回答,3:回复 }")
    @TableField("TYPE")
    private Integer type;

    @ApiModelProperty(value = "如果是提问,则有值,为提问标题")
    @TableField("TITLE")
    private String title;

    @ApiModelProperty(value = "内容")
    @TableField("CONTENT")
    private String content;

    @ApiModelProperty(value = "课程ID")
    @TableField("COURSE_ID")
    private Long courseId;

    @ApiModelProperty(value = "用户ID")
    @TableField("USER_ID")
    private Long userId;

    @ApiModelProperty(value = "讨论ID")
    @TableField("DISCUSS_ID")
    private Long discussId;

    @TableField("REPLY_DISCUSS_ID")
    private Long replyDiscussId;

    @TableField("REF_DISCUSS_ID")
    private Long refDiscussId;

    @TableField("ANSWER_COUNT")
    private Integer answerCount;

}
