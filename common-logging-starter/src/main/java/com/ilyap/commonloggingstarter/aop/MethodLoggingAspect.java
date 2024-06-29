package com.ilyap.commonloggingstarter.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Aspect
public class MethodLoggingAspect {

    @Pointcut("@within(com.ilyap.commonloggingstarter.annotation.Logged) || @annotation(com.ilyap.commonloggingstarter.annotation.Logged)")
    public void isLogged() {
    }

    @Around("isLogged()")
    public Object addLoggingAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String username = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            username = ((UserDetails) authentication.getPrincipal()).getUsername();
        }

        List<String> args = Arrays.stream(proceedingJoinPoint.getArgs())
                .map(Object::toString)
                .toList();
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        Object result = proceedingJoinPoint.proceed();
        log.info("User {} calls {}.{}({})", username, className, methodName, args);

        return result;
    }
}
