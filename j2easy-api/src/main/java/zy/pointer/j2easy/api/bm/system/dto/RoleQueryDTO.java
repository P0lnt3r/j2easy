package zy.pointer.j2easy.api.bm.system.dto;

import lombok.Data;
import zy.pointer.j2easy.business.system.entity.Role;
import zy.pointer.j2easy.framework.web.model.dto.PageQueryDTO;

@Data
public class RoleQueryDTO extends PageQueryDTO<Role> {

    private Long id;

    private String name;

    private Integer realm;

    private Integer state;

}
