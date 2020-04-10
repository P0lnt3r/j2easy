package zy.pointer.j2easy.api.bm.system.dto;

import lombok.Data;
import zy.pointer.j2easy.business.system.entity.Permission;
import zy.pointer.j2easy.framework.web.model.dto.AbsDataTransferObject;

@Data
public class PermissionDTO extends AbsDataTransferObject<Permission> {

    private Long id ;

    private String name;

    private String state;

}
