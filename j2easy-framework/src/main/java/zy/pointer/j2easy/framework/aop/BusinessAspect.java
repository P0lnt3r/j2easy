package zy.pointer.j2easy.framework.aop;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zy.pointer.j2easy.framework.log.annos.LogMethod;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 *  业务层切面
 */
@Component
@Aspect
@Slf4j
public class BusinessAspect {

    /**
     * 切入点:针对继承 AbsBusinessService 抽象实现的子类进行切面
     */
    @Pointcut( "execution(* zy.pointer.j2easy.business..*.*(..)) " +
            "|| execution(* zy.pointer.j2easy.framework.business.AbsBusinessService.*(..))")
    public void pointcut(){}

    @Autowired ObjectMapper mapper;

    @Around( "pointcut()" )
    public Object around( ProceedingJoinPoint proceedingJoinPoint ) throws Throwable {
        LogMethod logMethod = getLogMethodAnnotation( proceedingJoinPoint );
        Class entityClass   = getEntityClass(proceedingJoinPoint);
        Object result = null;
        Throwable throwable = null;
        Long beforeTime = System.currentTimeMillis();
        try {
            handleLogBefore( logMethod , entityClass , proceedingJoinPoint );
            result = proceedingJoinPoint.proceed();
        }catch (Throwable e){
            throwable = e;
            throw e;
        } finally {
            handleLogAfter( logMethod , entityClass , beforeTime , result , throwable );
        }
        return result;
    }

    /**
     * 处理日志记录
     */
    private void handleLogBefore( LogMethod logMethod , Class entityClass ,ProceedingJoinPoint proceedingJoinPoint ){
        if ( logMethod != null ){
            String actionName = getActionName( logMethod , entityClass );
            boolean logIn = logMethod.logIn();
            if ( !logIn ){
                log.info( "执行 => {} : {}" , actionName , "...." );
            }else{
                try {
                    log.info( "执行 => {} : {}" , actionName , mapper.writeValueAsString( proceedingJoinPoint.getArgs() ) );
                } catch (JsonProcessingException e) {
                    log.info( "执行 => {} : {}" , actionName , proceedingJoinPoint.getArgs() );
                }
            }

        }
    }

    private void handleLogAfter( LogMethod logMethod , Class entityClass , Long beforeTime , Object result ,Throwable e ){
        if ( logMethod != null ){
            String actionName = getActionName( logMethod , entityClass );
            Long cost = System.currentTimeMillis() - beforeTime;
            if ( e != null ){
                log.error("执行 <= {} X {} | ({})ms" , actionName , e.getMessage() , cost);
            }else{
                boolean logOut = logMethod.logOut();
                if ( !logOut ){
                    log.info ("执行 <= {} = {} | ({})ms" , actionName , "......" , cost);
                }else{
                    try {
                        log.info ("执行 <= {} = {} | ({})ms" , actionName , mapper.writeValueAsString(result) , cost);
                    } catch (JsonProcessingException e1) {
                        log.info ("执行 <= {} = {} | ({})ms" , actionName , result , cost);
                    }
                }
            }
        }
    }

    private String getActionName( LogMethod logMethod , Class entityClass ){
        String actionName = logMethod.name();
        ApiModel apiModel = null;
        String desc = "";
        if ( entityClass != null ){
            if ( (apiModel = (ApiModel)entityClass.getDeclaredAnnotation( ApiModel.class )) != null ){
                desc = apiModel.value();
            }else{
                desc = entityClass.getName();
            }
        }
        return StrUtil.format( actionName , desc );
    }

    private LogMethod getLogMethodAnnotation( ProceedingJoinPoint proceedingJoinPoint  ){
        Signature signature = proceedingJoinPoint.getSignature();
        MethodSignature methodSignature;
        if ( signature instanceof MethodSignature ){
            methodSignature = ( MethodSignature ) signature;
            Method method = methodSignature.getMethod();
            return method.getDeclaredAnnotation( LogMethod.class );
        }
        return null;
    }


    /**
     * 获取 Entity 泛型类
     * @return
     */
    private Class getEntityClass( ProceedingJoinPoint proceedingJoinPoint ) {
        Type _type = proceedingJoinPoint.getTarget().getClass().getGenericSuperclass();
        if ( _type instanceof ParameterizedType ){
            ParameterizedType type = (ParameterizedType)_type;
            // 获取子类申明的泛型组
            Type[] typeArr = type.getActualTypeArguments();
            if ( typeArr.length == 2 ){
                return (Class)type.getActualTypeArguments()[1];
            }
        }
        return null;
    }

}
