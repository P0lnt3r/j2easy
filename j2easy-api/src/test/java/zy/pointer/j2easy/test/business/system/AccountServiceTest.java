package zy.pointer.j2easy.test.business.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import zy.pointer.j2easy.business.system.entity.Account;
import zy.pointer.j2easy.business.system.service.IAccountService;
import zy.pointer.j2easy.test.SpringTestCase;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class AccountServiceTest extends SpringTestCase {

    @Autowired
    IAccountService accountService;

    @Test
    public void test(){
        Account account = new Account();
        account.setUsername("Hello");
        account.setUnlockTime(LocalDateTime.now());
        accountService.save(account);
    }

    @Test
    public void testQuery(){
        Map<String,Object> params = new HashMap<>();
        params.put("username" , "Hello");
        params.put("keywords" , "Hello");
        Page<Account> page = new Page<>();
        page.setCurrent( 1 );
        page.setSize( 5 );
        accountService.selectByMapForPage( page , params  )
                .getRecords().forEach( System.out::println );
        System.out.println(page.getRecords());
    }

}
