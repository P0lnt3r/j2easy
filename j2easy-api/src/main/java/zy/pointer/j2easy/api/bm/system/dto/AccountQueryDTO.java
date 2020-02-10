package zy.pointer.j2easy.api.bm.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import zy.pointer.j2easy.business.system.entity.Account;
import zy.pointer.j2easy.framework.web.model.dto.PageQueryDTO;

@ApiModel( "账户信息分页查询" )
@Data
public class AccountQueryDTO extends PageQueryDTO<Account> {

    @ApiModelProperty("账户名称")
    private String username;

    @ApiModelProperty("账户密码")
    private String password;

    @ApiModelProperty("账户状态:[1:正常,2:禁止]")
    private String state;

}
