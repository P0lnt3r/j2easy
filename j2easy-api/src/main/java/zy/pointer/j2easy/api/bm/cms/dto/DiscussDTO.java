package zy.pointer.j2easy.api.bm.cms.dto;

import lombok.Data;
import zy.pointer.j2easy.business.cms.entity.Discuss;
import zy.pointer.j2easy.framework.web.model.dto.AbsDataTransferObject;

@Data
public class DiscussDTO extends AbsDataTransferObject<Discuss> {

    private Long id ;

    private Integer type;

    private String title;

    private String content;

    private Long discussId;

    private String discussTitle;

    private Long courseId;

    private Long userId;

}
