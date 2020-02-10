package zy.pointer.j2easy.framework.web.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import zy.pointer.j2easy.framework.web.model.RequestContext;
import zy.pointer.j2easy.framework.web.model.TraceIDBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class LogTraceBuildInterceptor implements HandlerInterceptor {

    @Autowired ObjectMapper mapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // build trace for LOG
        RequestContext.Build( request );
        TraceIDBuilder.Build( request , RequestContext.ThreadLocal.get().getStartTime().getTime() + "" );
        // LOG INFO ...
        if( handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            /*
             * 获取 Controller 方法描述时 , @ApiOperation(Swagger2).value() 优先. @RequestMapper.name() 次之
             */
            String name ;
            ApiOperation apiOperation = handlerMethod.getMethodAnnotation( ApiOperation.class );
            if ( apiOperation != null ){
                name = apiOperation.value();
            }else{
                RequestMapping requestMapping = handlerMethod.getMethodAnnotation( RequestMapping.class );
                name = requestMapping.name();
            }
            log.info( "访问 {} " ,  name );
            log.info( "参数:{} " ,  RequestContext.ThreadLocal.get().getBody() );
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestContext requestContext = RequestContext.ThreadLocal.get();
        log.info( "响应:{} ({})ms" ,
                mapper.writeValueAsString( requestContext.getJsonResponse() ) ,
                System.currentTimeMillis() - requestContext.getStartTime().getTime()
        );
    }
}
