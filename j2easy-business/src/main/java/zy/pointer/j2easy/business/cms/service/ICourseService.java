package zy.pointer.j2easy.business.cms.service;

import zy.pointer.j2easy.business.cms.entity.Course;
import zy.pointer.j2easy.framework.business.BusinessService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhouyang
 * @since 2020-04-15
 */
public interface ICourseService extends BusinessService<Course> {

    List<Course> listCourseWithCount();

}
