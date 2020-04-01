package zy.pointer.j2easy.api.bm.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import zy.pointer.j2easy.business.system.entity.Dict;
import zy.pointer.j2easy.framework.web.model.dto.AbsDataTransferObject;

@Data
@ApiModel( "字典数据" )
public class DictSaveDTO extends AbsDataTransferObject<Dict> {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("唯一值")
    private String uniq;

    @ApiModelProperty("字典名称")
    private String name;

    @ApiModelProperty("字典值 , 同辈唯一")
    private String val;

    @ApiModelProperty("字典类型:[{1:字典值},{2:字典目录}]")
    private String type;

    @ApiModelProperty("父节点主键ID")
    private Long pId;

}
