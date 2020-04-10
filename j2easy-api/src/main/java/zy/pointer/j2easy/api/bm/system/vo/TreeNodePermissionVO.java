package zy.pointer.j2easy.api.bm.system.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import zy.pointer.j2easy.framework.datastructuers.pathtree.ReactAntdTreeNode;

import java.util.List;

@ApiModel
@Data
public class TreeNodePermissionVO extends ReactAntdTreeNode<TreeNodePermissionVO, PermissionVO> {

    private List<TreeNodePermissionVO> children;

    private PermissionVO payload;

    @Override
    public void setChildren(List children) {
        this.children = children;
    }

    @Override
    public void setPayload(PermissionVO payload) {
        this.payload = payload;
    }

}
