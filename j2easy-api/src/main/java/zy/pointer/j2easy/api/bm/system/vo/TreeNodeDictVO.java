package zy.pointer.j2easy.api.bm.system.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import zy.pointer.j2easy.framework.datastructuers.pathtree.ReactAntdTreeNode;

import java.util.List;

@ApiModel("字典数据")
@Data
public class TreeNodeDictVO extends ReactAntdTreeNode<TreeNodeDictVO, DictVO> {

    private List<TreeNodeDictVO> children;

    private DictVO payload;

    @Override
    public void setChildren(List children) {
        this.children = children;
    }

    @Override
    public void setPayload(DictVO payload) {
        this.payload = payload;
    }
}
