package zy.pointer.j2easy.api.bm.cms.dto;

import lombok.Data;
import zy.pointer.j2easy.business.cms.entity.Discuss;
import zy.pointer.j2easy.framework.web.model.dto.PageQueryDTO;

@Data
public class DiscussQueryDTO extends PageQueryDTO<Discuss> {

    private Integer type;

    private String userId;

    private String userName;

    private Long courseId;

    private Long discussId;

    private String discussTitle;

    private String title;

}
