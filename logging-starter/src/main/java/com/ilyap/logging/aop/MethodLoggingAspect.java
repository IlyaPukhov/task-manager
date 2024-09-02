package com.ilyap.logging.aop;

import com.ilyap.logging.annotation.Logged;
import com.ilyap.logging.config.LoggingProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.util.StopWatch;

/**
 * The {@code MethodLoggingAspect} is an AspectJ aspect responsible for logging method execution of methods
 * in classes and methods marked {@link Logged @Logged}.
 * <p>
 * It log information about the called method, the return value, and (optional) the execution time of the request.
 * </p>
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class MethodLoggingAspect {

    private final LoggingProperties properties;

    @Pointcut("@within(com.ilyap.logging.annotation.Logged) || @annotation(com.ilyap.logging.annotation.Logged)")
    public void isLogged() {
    }

    @Around("isLogged()" +
            "&& target(clazz)")
    public Object addLoggingAround(ProceedingJoinPoint proceedingJoinPoint, Object clazz) throws Throwable {
        Object result;
        String methodName = proceedingJoinPoint.getSignature().getName();
        try {
            String logMessage;
            if (properties.isTimeLogging()) {
                StopWatch stopWatch = new StopWatch();

                stopWatch.start();
                result = proceedingJoinPoint.proceed();
                stopWatch.stop();

                logMessage = "Invoked %s in class %s, result %s | exec in %s ms"
                        .formatted(methodName, clazz, result, stopWatch.getTotalTimeMillis());
            } else {
                result = proceedingJoinPoint.proceed();
                logMessage = "Invoked %s in class %s, result %s".formatted(methodName, clazz, result);
            }
            log.info(logMessage);
        } catch (Throwable ex) {
            log.error("Invoked {} in class {}, exception {}: {}", methodName, clazz, ex.getClass(), ex.getMessage());
            throw ex;
        }

        return result;
    }
}
