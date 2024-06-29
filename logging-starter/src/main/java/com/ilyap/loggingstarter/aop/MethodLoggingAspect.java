package com.ilyap.loggingstarter.aop;

import com.ilyap.loggingstarter.annotation.Logged;
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

/**
 * The {@code MethodLoggingAspect} is an AspectJ aspect responsible for logging method execution of methods
 * in classes and methods marked {@link Logged @Logged}.
 * <p>
 * It logs information about the called method (in the {@code className.methodName(args)} format)
 * and about the principal that called it.
 * </p>
 */
@Slf4j
@Aspect
public class MethodLoggingAspect {

    @Pointcut("@within(com.ilyap.loggingstarter.annotation.Logged) || @annotation(com.ilyap.loggingstarter.annotation.Logged)")
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
