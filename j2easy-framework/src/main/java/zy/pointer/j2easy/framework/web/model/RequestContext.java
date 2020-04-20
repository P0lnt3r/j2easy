package zy.pointer.j2easy.framework.web.model;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Enumeration;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 闭环封装 请求-响应 流程数据
 */
@Data
public class RequestContext {

    public static ThreadLocal< RequestContext > ThreadLocal = new ThreadLocal<>();

    /************************************************************/
    // IN :
    /** 构建时间,也即访问的起始时间 */
    private Date startTime;
    /** 请求信息 */
    private HttpServletRequest httpServletRequest;
    private String uri ;
    private String body ;
    private String headers;
    /************************************************************/

    /************************************************************/
    // OUT :
    private JSONResponse jsonResponse;
    private Exception e;
    /************************************************************/

    private RequestContext(){}

    private RequestContext( HttpServletRequest httpServletRequest ){
        this();
        this.startTime = new Date();
        this.httpServletRequest = httpServletRequest;
        this.uri = httpServletRequest.getRequestURI();
        Enumeration<String> parameterNames = httpServletRequest.getHeaderNames();
        StringBuilder headerBuilder = new StringBuilder();
        while( parameterNames.hasMoreElements() ){
            String header = parameterNames.nextElement();
            headerBuilder.append( header + ":" + httpServletRequest.getHeader( header ) + "\r\n" );
        }
        this.headers = headerBuilder.toString();

        this.body = httpServletRequest.getParameterMap().entrySet().stream()
                .map( entry -> entry.getKey() + "=" + Stream.of( entry.getValue() ).collect(Collectors.joining(",")) )
                .collect(Collectors.joining("&"));
        ThreadLocal.set(this);
    }

    public static RequestContext Build( HttpServletRequest httpServletRequest ){
        return new RequestContext( httpServletRequest );
    }

}
