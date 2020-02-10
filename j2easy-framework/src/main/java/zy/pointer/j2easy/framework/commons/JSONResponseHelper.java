package zy.pointer.j2easy.framework.commons;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zy.pointer.j2easy.framework.web.model.JSONResponse;

import static zy.pointer.j2easy.framework.web.model.JSONResponse.Build;

@Component
public class JSONResponseHelper {

    @Autowired I18N I18N;

    public JSONResponse success( Object data ){
        return Build( IErrorCode.SUCCESS_CODE , I18N.getI18NMessage( IErrorCode.SUCCESS_CODE ) , data );
    }

    public JSONResponse error ( IErrorCode errorCode ){
        return error(errorCode , "");
    }

    public JSONResponse error( IErrorCode errorCode , String extras ){
        String message = I18N.getI18NMessage( errorCode.code() );
        if (StrUtil.isNotEmpty(extras)){
            message += ":" + extras;
        }
        return JSONResponse.Build( errorCode.code() , message , "" );
    }

}
