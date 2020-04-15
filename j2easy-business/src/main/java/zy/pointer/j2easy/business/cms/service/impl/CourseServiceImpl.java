package zy.pointer.j2easy.business.cms.service.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.Transactional;
import zy.pointer.j2easy.business.cms.entity.Course;
import zy.pointer.j2easy.business.cms.mapper.CourseMapper;
import zy.pointer.j2easy.business.cms.service.ICourseService;
import zy.pointer.j2easy.framework.business.AbsBusinessService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhouyang
 * @since 2020-04-15
 */
@Service
@Primary
@Transactional
public class CourseServiceImpl extends AbsBusinessService<CourseMapper, Course> implements ICourseService {

    @Override
    public List<Course> listCourseWithCount() {
        return getBaseMapper().listCourseWithCount();
    }
}
