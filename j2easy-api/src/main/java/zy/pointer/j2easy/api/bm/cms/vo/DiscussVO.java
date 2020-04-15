package zy.pointer.j2easy.api.bm.cms.vo;

import lombok.Data;
import zy.pointer.j2easy.business.cms.entity.Discuss;
import zy.pointer.j2easy.framework.web.model.vo.AbsValueObject;

@Data
public class DiscussVO extends AbsValueObject<Discuss> {

    private Long id ;

    private String title;

    private String userName;

    private String userId;

    private String content;

    private Integer type;

}
