package zy.pointer.j2easy.api.bm.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import zy.pointer.j2easy.business.system.entity.Account;
import zy.pointer.j2easy.framework.web.model.dto.AbsDataTransferObject;

import java.time.LocalDateTime;

@ApiModel
@Data
public class AccountSaveDTO extends AbsDataTransferObject<Account> {

    @ApiModelProperty( value = "登陆账号")
    private String username;

    @ApiModelProperty( value = "登陆密码")
    private String password;

    public static void main(String[] args) {

        AccountSaveDTO dto = new AccountSaveDTO();

        dto.setUsername("123123");
        dto.setPassword("aabbcc");

        Account account = dto.convert();
        System.out.println(account);

        account = dto.convert( AccountSaveDTO.class , (dto1, account1) -> {
            account1 = dto1.convert();
            account1.setUnlockTime(LocalDateTime.now());
            return account1;
        } );
        System.out.println(account);

    }

}
