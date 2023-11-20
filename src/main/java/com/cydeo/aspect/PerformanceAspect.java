package com.cydeo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {

    @Pointcut("@annotation(com.cydeo.annotation.ExecutionTime)")
    public void executionTime() {}

    @Around("executionTime()")
    public Object aroundAnyExecutionTimeAdvise(ProceedingJoinPoint proceedingJoinPoint){
        Long beforeTime = System.currentTimeMillis();
        Object result = null;

        log.info("Execution starts:");try{
            result = proceedingJoinPoint.proceed();
        } catch (Throwable t){
            t.printStackTrace();
        }

        Long afterTime = System.currentTimeMillis();

        log.info("Time taken to execute: {} ms - Method: {}",
                (afterTime-beforeTime),
                proceedingJoinPoint.getSignature().toShortString()
        );
        return result;
    }

}
