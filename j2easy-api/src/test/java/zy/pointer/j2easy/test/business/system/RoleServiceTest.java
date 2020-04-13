package zy.pointer.j2easy.test.business.system;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import zy.pointer.j2easy.business.system.entity.Role;
import zy.pointer.j2easy.business.system.service.IRoleService;
import zy.pointer.j2easy.test.SpringTestCase;

public class RoleServiceTest extends SpringTestCase {

    @Autowired
    IRoleService roleService;

    @Test
    public void test() throws JsonProcessingException {
        Role role = roleService.getRoleWithPermission(1L);
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(role.getPermissionList()));
    }

}
