package zy.pointer.j2easy.test.cms;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import zy.pointer.j2easy.business.cms.service.ICourseService;
import zy.pointer.j2easy.test.SpringTestCase;

public class CourseServiceTest extends SpringTestCase {

    @Autowired
    ICourseService service;

    @Test
    public void test(){

        service.list().forEach( System.out::println );

    }


}
