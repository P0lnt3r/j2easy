package zy.pointer.j2easy.framework.datastructuers.pathtree;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class PathTreeNode <T> implements Serializable {

    public PathTreeNode( String path , String name , T payload ){
        this.path = path;
        this.name = name;
        this.payload = payload;
    }

    /**
     * 父类节点
     */
    @JsonIgnore
    private PathTreeNode parent;

    /**
     * 字辈集合
     */
    private List<PathTreeNode> childrens;

    /**
     * 排序索引
     */
    @JsonIgnore
    private Integer order;

    /**
     * 负载数据
     */
    @JsonIgnore
    private T payload;

    /**
     * 路径
     */
    private String path;

    /**
     * 节点名称
     */
    private String name;

    public PathTreeNode getParent() {
        return parent;
    }

    public void setParent(PathTreeNode parent) {
        this.parent = parent;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PathTreeNode> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<PathTreeNode> childrens) {
        this.childrens = childrens;
    }
}
