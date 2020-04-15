package zy.pointer.j2easy.api.bm.cms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zy.pointer.j2easy.api.bm.cms.vo.CourseVO;
import zy.pointer.j2easy.business.cms.service.ICourseService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bm/cms/course")
@Api("后台课程接口")
public class CourseController {

    @Autowired
    ICourseService courseService;

    @GetMapping("/list")
    @ApiOperation("获取课程列表")
    public List<CourseVO> list(){
        return courseService.listCourseWithCount()
                .stream()
                .map( course -> new CourseVO().from( course , CourseVO.class ))
                .collect( Collectors.toList() );
    }

}
