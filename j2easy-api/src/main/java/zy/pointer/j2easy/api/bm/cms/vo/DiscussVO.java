package zy.pointer.j2easy.api.bm.cms.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import zy.pointer.j2easy.business.cms.entity.Discuss;
import zy.pointer.j2easy.framework.web.model.vo.AbsValueObject;

import java.time.LocalDateTime;

@Data
public class DiscussVO extends AbsValueObject<Discuss> {

    private Long id ;

    private Integer type;

    private String title;

    private String content;

    private Integer replyCount;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime createTime;

    /***************************************/
    // 用户相关信息
    private String userId;

    private String userName;

    private Integer userType;
    /***************************************/

    /***************************************/
    // 课程相关信息
    private Long courseId;

    private String courseName;
    /***************************************/

    private String discussTitle;

    private String discussContent;

    private String discussRefContent;

    private String discussReplyContent;

}
