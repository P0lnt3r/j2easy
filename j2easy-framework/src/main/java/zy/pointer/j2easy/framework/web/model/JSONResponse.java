package zy.pointer.j2easy.framework.web.model;

import lombok.Data;

@Data
public class JSONResponse {

    private String code;

    private String message;

    private Object data;

    private JSONResponse(){
        code = "";
        message = "";
        data = "";
        RequestContext requestContext = RequestContext.ThreadLocal.get();
        if ( requestContext != null ){
            requestContext.setJsonResponse(this);
        }
    }

    public static JSONResponse Build( String code , String message, Object data ){
        JSONResponse jsonResponse = new JSONResponse();
        jsonResponse.setCode(code);
        jsonResponse.setMessage(message);
        jsonResponse.setData(data);
        return jsonResponse;
    }



}
