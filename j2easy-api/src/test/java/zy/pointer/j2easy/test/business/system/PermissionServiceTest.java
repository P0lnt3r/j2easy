package zy.pointer.j2easy.test.business.system;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import zy.pointer.j2easy.business.system.service.IPermissionService;
import zy.pointer.j2easy.test.SpringTestCase;

public class PermissionServiceTest extends SpringTestCase {

    @Autowired
    IPermissionService permissionService;

    @Test
    public void test(){

        permissionService.initRealmsPermission();
    }


}
