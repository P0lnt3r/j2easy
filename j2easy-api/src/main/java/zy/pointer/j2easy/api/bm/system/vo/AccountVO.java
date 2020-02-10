package zy.pointer.j2easy.api.bm.system.vo;

import lombok.Data;
import zy.pointer.j2easy.business.system.entity.Account;
import zy.pointer.j2easy.framework.web.model.vo.AbsValueObject;

@Data
public class AccountVO extends AbsValueObject<Account> {

    private String username;

    private String password;

    public static void main(String[] args) {
        Account account = new Account();
        account.setUsername("Hello");
        AccountVO VO = new AccountVO().from( account , AccountVO.class , (_account, vo) -> {
            vo.setPassword( _account.getUsername() );
            return vo;
        } );
        System.out.println(VO);
    }

}
