package zy.pointer.j2easy.business.cms.mapper;

import zy.pointer.j2easy.business.cms.entity.Course;
import zy.pointer.j2easy.framework.repository.RepositoryMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhouyang
 * @since 2020-04-15
 */
public interface CourseMapper extends RepositoryMapper<Course> {

    List<Course> listCourseWithCount();

}
