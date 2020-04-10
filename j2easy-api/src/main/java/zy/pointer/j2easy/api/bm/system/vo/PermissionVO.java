package zy.pointer.j2easy.api.bm.system.vo;

import lombok.Data;
import zy.pointer.j2easy.business.system.entity.Permission;
import zy.pointer.j2easy.framework.web.model.vo.AbsValueObject;
import zy.pointer.j2easy.framework.web.model.vo.ValueObject;

@Data
public class PermissionVO extends AbsValueObject<Permission> {

    private Long id;

    private String name;

    private String value;

    private String path;

    private String type;

    private String state;

}
