package se.yrgo.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceTimingAdvice {

    @Around("execution(* se.yrgo.services..*.*(..)) || execution(* se.yrgo.dataaccess.*.*(..))")
    public Object performTimingMeasurement(ProceedingJoinPoint method) throws Throwable {
        long startTime = System.nanoTime();

        try {
            Object value = method.proceed();
            return value;
        } finally {
            long endTime = System.nanoTime();
            long timeTaken = endTime - startTime;
            System.out.println("Time taken for the method " + method.getSignature().getName()
                    + " from the class " + method.getTarget().getClass().getName()
                    + " took " + timeTaken / 1000000 + " milliseconds");
        }
    }
}
