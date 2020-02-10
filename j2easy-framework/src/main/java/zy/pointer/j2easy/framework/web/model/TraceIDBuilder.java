package zy.pointer.j2easy.framework.web.model;

import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;

public class TraceIDBuilder {

    /**
     *
     * @param request
     * @return [{client}-{ip}-{-|=>}{uri}|{#extra}]
     */
    public static String Build(HttpServletRequest request , String extra ){
        String client = "client";
        String userAgent = request.getHeader("User-Agent");
        if( userAgent.contains("/") ){
            client = userAgent.substring( 0 , userAgent.indexOf("/") );
        }else{
            client = userAgent.substring(0 , client.length() >= 6 ? 6 : client.length());
        }
        String ip     = request.getRemoteAddr();
        String method = request.getMethod().toUpperCase().equals("GET") ? "->" : "=>";
        String uri    = request.getRequestURI();
        String trace = " TRACE:[" + client + "]-[" + ip + "]" + method + uri + "#"+extra+"# ";
        MDC.put( "trace" , trace );
        return trace;
    }

}
