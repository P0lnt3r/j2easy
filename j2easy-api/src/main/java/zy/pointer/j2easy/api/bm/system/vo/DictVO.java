package zy.pointer.j2easy.api.bm.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import zy.pointer.j2easy.business.system.entity.Dict;
import zy.pointer.j2easy.framework.web.model.vo.AbsValueObject;
import zy.pointer.j2easy.framework.web.model.vo.ValueObject;

@Data
@ApiModel("数据字典值对象")
public class DictVO extends AbsValueObject<Dict> {

    @ApiModelProperty("字典ID")
    private Long id ;

    @ApiModelProperty("字典类型")
    private String type;

    @ApiModelProperty("字典名称")
    private String name;

    @ApiModelProperty("字典全局唯一值")
    private String uniq;

    @ApiModelProperty("同辈唯一字典值")
    private String val;

}
