package zy.pointer.j2easy.api.bm.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import zy.pointer.j2easy.business.system.entity.Role;
import zy.pointer.j2easy.framework.web.model.dto.AbsDataTransferObject;

@Data
@ApiModel
public class RoleDTO extends AbsDataTransferObject<Role> {

    @ApiModelProperty("角色ID")
    private Long id;

    @ApiModelProperty("角色名称")
    private String name;

    @ApiModelProperty("角色状态")
    private Integer state;

    @ApiModelProperty("角色所属域")
    private Integer realm;

}
