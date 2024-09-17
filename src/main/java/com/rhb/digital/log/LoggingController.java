package com.rhb.digital.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

//import javax.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Aspect
@Component
public class LoggingController {

    private static final Logger logger = LoggerFactory.getLogger(LoggingController.class);

    //@Before("execution(* com.rhb.digital.controller..*(..)) && @annotation(requestMapping)")
    @Before("execution(* com.rhb.digital.controller..*(..))")
    public void logBefore(JoinPoint joinPoint) {
        // Access HttpServletRequest using RequestContextHolder
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String headers = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Log request details
        logger.info("Incoming Request:- "+" URL: " + url+"\nMethod: " + method+"\nHeaders: " + headers+"\nArguments: " + joinPoint.getArgs());

    }
    @AfterReturning(pointcut = "execution(* com.rhb.digital.controller..*(..))", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        if (result instanceof Mono) {
            ((Mono<?>) result).doOnNext(response -> logger.info("Response:- " +
                            "\nMethod: " + joinPoint.getSignature().getName() +
                            "\nResponse Body: " + response))
                    .doOnError(error -> logger.error("Error occurred: " + error.getMessage()))
                    .subscribe(); // Make sure to subscribe to process the Mono
        } else if (result instanceof Flux) {
            ((Flux<?>) result).doOnNext(response -> logger.info("Response:- " +
                            "\nMethod: " + joinPoint.getSignature().getName() +
                            "\nResponse Body: " + response))
                    .doOnError(error -> logger.error("Error occurred: " + error.getMessage()))
                    .subscribe(); // Make sure to subscribe to process the Flux
        } else {
            // Log non-reactive responses
            logger.info("Response:- " +
                    "\nMethod: " + joinPoint.getSignature().getName() +
                    "\nResponse Body: " + result);
        }
    }
}