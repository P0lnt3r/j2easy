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
    ASSET_FLOW_TYPE_ADD_SELLINCOME( "售卖收益" , "asset.flow.type.add.sellincome" , "流水增加详情:售卖收益" ),
    ASSET_FLOW_TYPE_ADD_TRANSIN( "资产转入" , "asset.flow.type.add.transin" , "流水增加详情:资产转入" ),
    ASSET_FLOW_TYPE_SUB( "2" , "asset.flow.type.sub" , "流水类型:减少" ),
    ASSET_FLOW_TYPE_SUB_MORTGAGE( "资产抵押" , "asset.flow.type.sub.mortgage" , "流水减少详情:资产抵押" ),
    ASSET_FLOW_TYPE_SUB_PAY( "购买支出" , "asset.flow.type.sub.buy" , "流水减少详情:购买支出" ),
    ASSET_FLOW_TYPE_SUB_TRANSOUT( "资产转出" , "asset.flow.type.sub.transout" , "流水减少详情:资产转出" ),

    ASSET_TYPE_CREDIT( "0" , "asset.type.credit" , "资产类型:信用资产" ),
    ASSET_TYPE_TOKEN( "1" , "asset.type.token" , "资产类型:通证资产" ),

    ASSET_TRADE_STATE_SELLOUT( "0" , "asset.trade.state.sellout" , "订单状态:售罄" ),
    ASSET_TRADE_STATE_SELLING( "1" , "asset.trade.state.selling" , "订单状态:在售" ),

    ASSET_TRANSFER_STATE_WAIT( "0" , "asset.transfer.state.wait" , "资产交易状态:待确认" ),
    ASSET_TRANSFER_STATE_CONFIRMED( "1" , "asset.transfer.state.confirmed" , "资产交易状态:已确认" ),

    ASSET_TRANSFER_TYPE_DOTRASFER( "1" , "asset.transfer.type.dotransfer" , "资产转让类型:转让" ),
    ASSET_TRANSFER_TYPE_DOTRADE(  "2" , "asset.trasfer.type.dotrade" , "资产转让类型:交易"),

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
