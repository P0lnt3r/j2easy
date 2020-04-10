package zy.pointer.j2easy.framework.datastructuers.pathtree;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * https://ant.design/components/tree-cn/
 * @param <T>
 */
@Data
@JsonInclude( JsonInclude.Include.NON_NULL )
public abstract class ReactAntdTreeNode<T extends SelfRefChildrenListObject , VO> extends SelfRefChildrenListObject {

    /**
     * 为 React - Antd - Treenode 设置树节点的负载数据
     * @param payload
     */
    public abstract void setPayload( VO payload );

    private Boolean checkable = null;

    private Boolean disabledCheckbox = false;

    private Boolean disabled = false;

    private String icon = null;

    private Boolean isLeaf = false;

    private String key;

    private Boolean selectable = true;

    private String title;

}
