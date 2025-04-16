package com.movewave.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Controller 계층의 API 호출에 대한 로깅을 담당하는 Aspect
 * - API 메서드 실행 시간을 측정하고 로깅
 * - 메서드명, 실행시간, 시작/종료 시간, 인자값 등을 기록
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    /**
     * Controller 패키지 내의 모든 메서드 실행을 가로채서 로깅
     * - com.movewave.*.controller..* : 직계 하위 컨트롤러
     * - com.movewave.*.*.controller..* : 2단계 하위 컨트롤러
     */
    @Around("execution(* com.movewave.*.controller..*(..)) || execution(* com.movewave.*.*.controller..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        try {
            Object proceed = joinPoint.proceed();
            return proceed;
        } finally {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            log.info(
                    "API Call - Method: {}, Duration: {} ms, Start Time: {}, End Time: {}, Arguments: {}",
                    joinPoint.getSignature(),
                    duration, 
                    startTime,
                    endTime,
                    Arrays.toString(joinPoint.getArgs())
            );
        }
    }

}