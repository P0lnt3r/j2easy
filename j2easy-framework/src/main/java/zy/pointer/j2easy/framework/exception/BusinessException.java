package zy.pointer.j2easy.framework.exception;

import zy.pointer.j2easy.framework.commons.IErrorCode;

/**
 * 业务层异常
 */
public class BusinessException extends CustomException {

    private IErrorCode code;

    private String extraMsg;

    public BusinessException(IErrorCode code) {
        this.code = code;
    }

    public BusinessException(IErrorCode code, String extraMsg) {
        this.code = code;
        this.extraMsg = extraMsg;
    }

    public IErrorCode getCode() {
        return code;
    }

    public void setCode(IErrorCode code) {
        this.code = code;
    }

    public String getExtraMsg() {
        return extraMsg;
    }

    public void setExtraMsg(String extraMsg) {
        this.extraMsg = extraMsg;
    }
}
