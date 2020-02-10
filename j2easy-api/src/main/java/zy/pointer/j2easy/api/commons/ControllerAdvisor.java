package zy.pointer.j2easy.api.commons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import zy.pointer.j2easy.business.commons.ErrorCode;
import zy.pointer.j2easy.framework.commons.JSONResponseHelper;
import zy.pointer.j2easy.framework.exception.RepeatRequestAccessException;
import zy.pointer.j2easy.framework.exception.jwt.JWTExpireException;
import zy.pointer.j2easy.framework.exception.jwt.JWTInvalidException;
import zy.pointer.j2easy.framework.exception.jwt.JWTKickOutException;

import java.util.Collection;
import java.util.stream.Collectors;


@RestControllerAdvice
public class ControllerAdvisor implements ResponseBodyAdvice {

    @Autowired
    JSONResponseHelper jsonResponseHelper;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return returnType.getDeclaringClass().getDeclaredAnnotation(RestController.class) != null ;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        return jsonResponseHelper.success(body);
    }

    @ExceptionHandler( Exception.class )
    public Object handle( Exception e ){
        e.printStackTrace();
        return null;
    }

    /**
     * @Valid 数据参数异常
     * @param e
     * @return
     */
    @ExceptionHandler( BindException.class )
    public Object handle( BindException e ){
        Collection error = e.getFieldErrors().stream().map( err -> err.getField() + ":" + err.getDefaultMessage())
                .collect(Collectors.toList());
        return jsonResponseHelper.error(ErrorCode.ERROR_CODE_1001 , error.toString());
    }

    /**
     * @Valid 方法访问方式错误异常 [405]
     * @param e
     * @return
     */
    @ExceptionHandler( { HttpRequestMethodNotSupportedException.class , NoHandlerFoundException.class})
    public Object handleCommonException( Exception e ){
        // 405
        if ( e instanceof HttpRequestMethodNotSupportedException ){
            return jsonResponseHelper.error(ErrorCode.ERROR_CODE_1405);
        }
        // 404
        return jsonResponseHelper.error(ErrorCode.ERROR_CODE_1404);
    }

    /**
     * 重复访问异常
     * @param e
     * @return
     */
    @ExceptionHandler( { RepeatRequestAccessException.class })
    public Object handleRepeatRequestAccessException( RepeatRequestAccessException e ){
        return jsonResponseHelper.error(ErrorCode.ERROR_CODE_1002 , e.getMessage());
    }

    /**
     * JWT 异常处理
     * @param e
     * @return
     */
    @ExceptionHandler( { JWTInvalidException.class , JWTKickOutException.class , JWTExpireException.class} )
    public Object handleJWTException( Exception e ){
        if ( e instanceof JWTKickOutException ){
            return jsonResponseHelper.error(ErrorCode.ERROR_CODE_1003);
        }
        if ( e instanceof JWTExpireException ){
            return jsonResponseHelper.error(ErrorCode.ERROR_CODE_1004);
        }
        return jsonResponseHelper.error(ErrorCode.ERROR_CODE_1005);
    }

}
