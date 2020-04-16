package zy.pointer.j2easy.api.bm.cms.vo;

import lombok.Data;
import zy.pointer.j2easy.business.cms.entity.Course;
import zy.pointer.j2easy.framework.web.model.vo.AbsValueObject;

@Data
public class CourseVO extends AbsValueObject<Course> {

    private Long id;

    private String name;

    private Integer questions;

    private Integer replies;

}
