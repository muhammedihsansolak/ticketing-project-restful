package com.cydeo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.cydeo.controller.ProjectController.*()) || " +
              "execution(* com.cydeo.controller.TaskController.*())")
    public void anyProjectAndTaskController(){}

    @Before("anyProjectAndTaskController()")
    public void beforeAnyProjectAndTaskControllerAdvise(JoinPoint joinPoint){
        log.info("Before -> Method: {}, User: {}",
                joinPoint.getSignature().toShortString(),
                getUsername()
                );
    }

    @AfterReturning(pointcut = "anyProjectAndTaskController()", returning = "result")
    public void afterAnyProjectAndTaskControllerAdvise(JoinPoint joinPoint, Object result){
        log.info("After Returning -> Method: {}, User: {}, Results: {}",
                joinPoint.getSignature().toShortString(),
                getUsername(),
                result.toString()
        );
    }

    @AfterThrowing(pointcut = "anyProjectAndTaskController()", throwing = "exception")
    public void afterAnyProjectAndTaskControllerAdvise(JoinPoint joinPoint, Exception exception){
        log.info("After Returning -> Method: {}, User: {}, Results: {}",
                joinPoint.getSignature().toShortString(),
                getUsername(),
                exception.toString()
        );
    }

    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SimpleKeycloakAccount details = (SimpleKeycloakAccount) authentication.getDetails();
        return details.getKeycloakSecurityContext().getToken().getPreferredUsername();
    }
}
