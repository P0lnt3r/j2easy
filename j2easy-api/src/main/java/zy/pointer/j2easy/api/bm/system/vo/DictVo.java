package zy.pointer.j2easy.api.bm.system.vo;

import lombok.Data;
import zy.pointer.j2easy.business.system.entity.Dict;
import zy.pointer.j2easy.framework.web.model.vo.AbsValueObject;
import zy.pointer.j2easy.framework.web.model.vo.ValueObject;

@Data
public class DictVo extends AbsValueObject<Dict> {

    private Long id ;

    private String type;

    private String name;

    private String uniq;

    private String val;

}
