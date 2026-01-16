package com.ey.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger =
            LoggerFactory.getLogger(LoggingAspect.class);

    // ================= POINTCUTS =================

    // All controller methods
    @Pointcut("execution(* com.ey.controller..*(..))")
    public void controllerMethods() {}

    // All service methods
    @Pointcut("execution(* com.ey.service..*(..))")
    public void serviceMethods() {}

    // ================= ADVICES =================

    // Before method execution
    @Before("controllerMethods() || serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Entering method: {}",
                joinPoint.getSignature().toShortString());
    }

    // After successful return
    @AfterReturning(
            pointcut = "controllerMethods() || serviceMethods()",
            returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("Exiting method: {}",
                joinPoint.getSignature().toShortString());
    }

    // After exception
    @AfterThrowing(
            pointcut = "controllerMethods() || serviceMethods()",
            throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        logger.error(
                "Exception in method: {} | Message: {}",
                joinPoint.getSignature().toShortString(),
                exception.getMessage()
        );
    }
}
