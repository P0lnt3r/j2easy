package zy.pointer.j2easy.api.bm.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import zy.pointer.j2easy.framework.datastructuers.pathtree.SelfRefChildrenListObject;

import java.util.List;

@ApiModel("字典数据")
public class TreeDictVo extends SelfRefChildrenListObject {

    private List<TreeDictVo> children;

    @ApiModelProperty("字典标题")
    private String title;

    @ApiModelProperty("字典唯一值")
    private String key;

    @ApiModelProperty("字典主键ID")
    private Long id;

    @Override
    public void setChildren(List children) {
        this.children = children;
    }

    public List<TreeDictVo> getChildren() {
        return children;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
