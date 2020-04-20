package zy.pointer.j2easy.api.bm.cms.vo;

import lombok.Data;
import zy.pointer.j2easy.business.cms.entity.User;
import zy.pointer.j2easy.framework.web.model.vo.AbsValueObject;

import java.time.LocalDateTime;

@Data
public class UserVO extends AbsValueObject<User> {

    private Long userid;

    private String name;

    private Integer type;

    private LocalDateTime registTime;

}
