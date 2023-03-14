//package eu.senla.aspect;
//
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import java.util.Collections;
//import java.util.stream.Collectors;
//
//@Component
//@Aspect
//@Slf4j
//public class LoggingAspect {
//
//    @Autowired
//    public LoggingAspect() {}
//
//    @Pointcut("@within(org.springframework.stereotype.Repository)")
//    public void controller() {}
//
//    @Around("controller()")
//    public Object logRequestAndResponse(ProceedingJoinPoint joinPoint) throws Throwable {
//
//
//        Object result = joinPoint.proceed();
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//        String requestInfo = String.format("%s %s", request.getMethod(), request.getRequestURL().toString());
//        log.info("Request Info : " + requestInfo);
//
//        log.info("Response : " + result);
//
//        return result;
//    }
//}
