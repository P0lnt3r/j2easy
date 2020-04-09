package zy.pointer.j2easy.api.bm.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import zy.pointer.j2easy.framework.datastructuers.pathtree.SelfRefChildrenListObject;

import java.util.List;

@ApiModel
@Data
public class TreePermissionVo extends SelfRefChildrenListObject {

    private List<TreePermissionVo> children;

    public List<TreePermissionVo> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List children) {
        this.children = children;
    }

    @ApiModelProperty("权限名称")
    private String title;

    @ApiModelProperty("权限唯一值")
    private String key;

    @ApiModelProperty("权限主键ID")
    private Long id;

}
