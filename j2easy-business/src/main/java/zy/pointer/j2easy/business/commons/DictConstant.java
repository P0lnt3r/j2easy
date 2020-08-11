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

    ASSET_FLOW_TYPE_ADD( "1" , "asset.flow.type.add" , "流水类型:增加" ),
    ASSET_FLOW_TYPE_ADD_ISSUE( "资产发行" , "asset.flow.type.add.issue" , "流水增加详情:资产发行" ),
    ASSET_FLOW_TYPE_SUB( "2" , "asset.flow.type.sub" , "流水类型:减少" ),
    ASSET_FLOW_TYPE_SUB_MORTGAGE( "资产抵押" , "asset.flow.type.sub.mortgage" , "流水减少详情:资产抵押" ),

    ASSET_TYPE_CREDIT( "0" , "asset.type.credit" , "资产类型:信用资产" ),
    ASSET_TYPE_TOKEN( "1" , "asset.type.token" , "资产类型:通证资产" ),

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
