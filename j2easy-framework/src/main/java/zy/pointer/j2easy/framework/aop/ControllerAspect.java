package zy.pointer.j2easy.framework.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import zy.pointer.j2easy.framework.web.model.JSONResponse;

/**
 * 控制层切面
 */
@Slf4j
public class ControllerAspect {

    /**
     * 切入点:针对继承 AbsBusinessService 抽象实现的子类进行切面
     */
    @Pointcut( "execution(* zy.pointer.j2easy.api..*.*Controller.*(..)) " )
    public void pointcut(){}


}
