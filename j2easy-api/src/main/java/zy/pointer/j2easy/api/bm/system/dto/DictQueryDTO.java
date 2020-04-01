package zy.pointer.j2easy.api.bm.system.dto;

import lombok.Data;
import zy.pointer.j2easy.business.system.entity.Dict;
import zy.pointer.j2easy.framework.web.model.dto.PageQueryDTO;

@Data
public class DictQueryDTO extends PageQueryDTO<Dict> {

    private Long pId;

    private String name;

    private String uniq;

    private String val;

}
