package zy.pointer.j2easy.business.commons;

/**
 *
 * 字典常量定义
 */
public enum  DictConstant {

    SYSTEM_PERMISSION_TYPE_MENU( "1" , "system.permission.type.menu" , "权限类型:菜单" ),
    SYSTEM_PERMISSION_TYPE_FUNC( "2" , "system.permission.type.func" , "权限类型:功能" ),

    SYSTEM_PERMISSION_STATE_ACTIVE( "1" , "system.permission.state.active" , "权限状态:正常" ),
    SYSTEM_PERMISSION_STATE_FORBIDDEN( "2" , "system.permission.state.forbidden" , "权限状态:禁用" ),
    SYSTEM_PERMISSION_STATE_PUBLIC( "3" , "system.permission.state.public" , "权限状态:公开" ),

    NULL( "","","" );

    public String value , code , desc ;

    DictConstant(String value , String code, String desc ) {
        this.value = value;
        this.code = code;
        this.desc = desc;
    }



    public String code() {
        return code;
    }

    public String desc() {
        return desc;
    }

    public String value() {
        return value;
    }

}
