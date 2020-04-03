package zy.pointer.j2easy.api.bm.system.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import zy.pointer.j2easy.business.system.entity.Account;
import zy.pointer.j2easy.framework.web.model.vo.AbsValueObject;

import java.time.LocalDateTime;

@Data
@ApiModel
public class AccountVO extends AbsValueObject<Account> {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("登录密码")
    private String password;

    @ApiModelProperty("账户创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("账户状态")
    private String state;

    @ApiModelProperty("所属域")
    private String realm;

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
