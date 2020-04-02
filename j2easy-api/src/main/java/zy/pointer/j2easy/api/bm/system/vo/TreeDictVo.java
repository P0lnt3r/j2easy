package zy.pointer.j2easy.api.bm.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import zy.pointer.j2easy.framework.datastructuers.pathtree.SelfRefChildrenListObject;

import java.util.List;

@ApiModel("字典数据")
@Data
public class TreeDictVo extends SelfRefChildrenListObject {

    private List<TreeDictVo> children;

    @ApiModelProperty("字典标题")
    private String title;

    @ApiModelProperty("字典唯一值")
    private String key;

    @ApiModelProperty("字典主键ID")
    private Long id;

    @ApiModelProperty("字典唯一标识值")
    private String uniq;

    @Override
    public void setChildren(List children) {
        this.children = children;
    }
}
