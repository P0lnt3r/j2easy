package zy.pointer.j2easy.business.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import zy.pointer.j2easy.framework.repository.BaseEntity;

import java.util.List;

@TableName("tb_system_role")
@Data
public class Role extends BaseEntity {

    @TableField("NAME")
    private String name ;

    @TableField("REALM")
    private Integer realm;

    @TableField("STATE")
    private Integer state;

    @TableField( exist = false )
    private List<Permission> permissionList;

}
