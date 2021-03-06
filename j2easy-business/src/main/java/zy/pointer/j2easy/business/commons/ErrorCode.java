package zy.pointer.j2easy.business.commons;

import zy.pointer.j2easy.framework.commons.IErrorCode;

public enum  ErrorCode implements IErrorCode {

    ERROR_CODE_SUCCESS( IErrorCode.SUCCESS_CODE , IErrorCode.SUCCESS_DESC ),

    ERROR_CODE_1001( "1001" , "参数校验异常" ),
    ERROR_CODE_1002( "1002" , "重复访问异常" ),
    ERROR_CODE_1003( "1003" , "您的账户在其他地方登陆"),
    ERROR_CODE_1004( "1004" , "用户信息失效,请重新登陆" ),
    ERROR_CODE_1005( "1005" , "认证信息错误,请重新登陆" ),
    ERROR_CODE_1404( "1404" , "404 - 资源不存在" ),
    ERROR_CODE_1405( "1405" , "方法访问错误"),

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
