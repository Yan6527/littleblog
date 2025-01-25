package com.atyan.aspect;

import com.alibaba.fastjson.JSON;
import com.atyan.annotation.mySystemlog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * AOP切面类，用于拦截添加了@mySystemlog注解的方法并记录日志信息
 */
@Component
@Aspect//告诉Spring容器，myLogAspect是切面类
@Slf4j//用于在控制台打印日志信息
public class myLogAspect {

    /**
     * 切点方法，匹配添加了@mySystemlog注解的方法
     */
    @Pointcut("@annotation(com.atyan.annotation.mySystemlog)")
    public void pointCut(){
    }

    /**
     * 环绕通知方法，用于在切点方法执行前后添加额外逻辑
     *
     * @param joinPoint 切入点对象，包含被增强方法的信息
     * @return 被增强方法的执行结果
     * @throws Throwable 转发异常
     */
    @Around("pointCut()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object ret;
        try {
            handleBefore(joinPoint);
            ret = joinPoint.proceed();//执行目标方法
            handleAfter(ret);
        } finally {
            log.info("=======================end=======================" + System.lineSeparator());
        }
        return ret;
    }

    /**
     * 前置通知方法，记录方法执行前的日志信息
     *
     * @param joinPoint 切入点对象，包含被增强方法的信息
     */
    private void handleBefore(ProceedingJoinPoint joinPoint) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        mySystemlog systemlog = getSystemlog(joinPoint);

        log.info("======================Start======================");
        log.info("请求URL   : {}", request.getRequestURL());
        log.info("接口描述   : {}", systemlog.businessName());
        log.info("请求方式   : {}", request.getMethod());
        log.info("请求类名   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), ((MethodSignature) joinPoint.getSignature()).getName());
        log.info("访问IP    : {}", request.getRemoteHost());
        List<String> argStrings = new ArrayList<>();
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (arg instanceof MultipartFile) {
                MultipartFile file = (MultipartFile) arg;
                argStrings.add(String.format("MultipartFile{name='%s', size=%d}", file.getName(), file.getSize()));
            } else {
                argStrings.add(JSON.toJSONString(arg));
            }
            log.info("传入参数   : {}", argStrings);
        }

//        log.info("传入参数   : {}", JSON.toJSONString(joinPoint.getArgs()));
    }

    /**
     * 后置通知方法，记录方法执行后的日志信息
     *
     * @param ret 目标方法的返回值
     */
    private void handleAfter(Object ret) {

//            log.info("返回结果   : {}", JSON.toJSONString(ret));
        if (ret instanceof MultipartFile) {
            MultipartFile file = (MultipartFile) ret;
            log.info("返回值 : MultipartFile{name='{}', size={}}", file.getName(), file.getSize());
        } else {
            log.info("返回值 : {}", JSON.toJSONString(ret));
        }
    }


    /**
     * 获取被增强方法上的mySystemlog注解对象
     *
     * @param joinPoint 切入点对象，包含被增强方法的信息
     * @return mySystemlog注解对象
     */
    private mySystemlog getSystemlog(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        mySystemlog systemlog = methodSignature.getMethod().getAnnotation(mySystemlog.class);
        return systemlog;
    }
}
