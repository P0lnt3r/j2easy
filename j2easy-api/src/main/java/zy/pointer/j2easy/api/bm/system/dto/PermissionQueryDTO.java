package zy.pointer.j2easy.api.bm.system.dto;

import lombok.Data;
import zy.pointer.j2easy.business.system.entity.Account;
import zy.pointer.j2easy.business.system.entity.Permission;
import zy.pointer.j2easy.framework.web.model.dto.PageQueryDTO;

@Data
public class PermissionQueryDTO extends PageQueryDTO<Permission> {

    private Long pId;

    private String name;

    private String value;

    private String path;

    private String state;

    private String type;

}
