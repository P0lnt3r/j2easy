package zy.pointer.j2easy.business.commons;

import zy.pointer.j2easy.framework.commons.IErrorCode;

public enum  ErrorCode implements IErrorCode {

    ERROR_CODE_SUCCESS( IErrorCode.SUCCESS_CODE , IErrorCode.SUCCESS_DESC ),

    ERROR_CODE_1001( "1001" , "参数校验异常" ),
    ERROR_CODE_1002( "1002" , "重复访问异常" ),
    ERROR_CODE_1003( "1003" , "您的账户在其他地方登陆"),
    ERROR_CODE_1004( "1004" , "用户信息失效,请重新登陆" ),
    ERROR_CODE_1005( "1005" , "认证信息错误,请重新登陆" ),
    ERROR_CODE_1006( "1006" , "用户密码不匹配" ),
    ERROR_CODE_1404( "1404" , "404 - 资源不存在" ),
    ERROR_CODE_1405( "1405" , "方法访问错误"),

    ERROR_CODE_2001( "2001" , "资产发行失败,资产名称或资产简称已被使用" ),
    ERROR_CODE_2002( "2002" , "资产发行失败,账户信用资产不足"),
    ERROR_CODE_2003( "2003" , "资产出售失败,可用资产不足" ),
    ERROR_CODE_2004( "2004" , "资产转让失败,可用资产不足" ),
    ERROR_CODE_2005( "2005" , "资产购买失败,可用资产不足" ),
    ERROR_CODE_2006( "2006" , "资产购买失败,订单内剩余购买数量不足,请刷新重试" ),
    ERROR_CODE_2007( "2007" , "资产购买失败,订单不存在,请刷新重试" ),
    ERROR_CODE_2008( "2008" , "资产转让失败,钱包地址无效" ),



    // 占位
    ERROR_CODE_NULL( "" , "" );

    ErrorCode(String code, String desc ) {
        this.code = code;
        this.desc = desc;
    }
    public String code , desc ;

    @Override
    public String code() {
        return code;
    }

    @Override
    public String desc() {
        return desc;
    }
}
