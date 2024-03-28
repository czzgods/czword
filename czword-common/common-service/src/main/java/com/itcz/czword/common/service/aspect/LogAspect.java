package com.itcz.czword.common.service.aspect;

import com.alibaba.fastjson.JSONObject;
import com.itcz.czword.common.service.annotation.SystemLog;
import com.itcz.czword.common.service.commonService.LogService;
import com.itcz.czword.common.service.exception.BusinessException;
import com.itcz.czword.common.utils.HttpUtil;
import com.itcz.czword.common.utils.UserContextUtil;
import com.itcz.czword.model.entity.log.Log;
import com.itcz.czword.model.entity.user.User;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Component
@Aspect
@Order(0)
@Slf4j
public class LogAspect {
    @Resource
    private AsyncTaskExecutor asyncTaskExecutor;
    @Resource
    private LogService logService;
    @Pointcut("@annotation(com.itcz.czword.common.service.annotation.SystemLog)")
    public void pt(){}

    @Around("pt()")
    public Object AroundLog(ProceedingJoinPoint joinPoint){
        log.error("日志切面执行了");
        Log systemLog = new Log();
        /*User user = UserContextUtil.getUser();
        if (user != null) {
            //说明用户登录了
            //设置用户id为日志的主键id
            systemLog.setId(user.getId());
        }*/
        //获取当前时间
        LocalDateTime dateTime = LocalDateTime.now();
        systemLog.setCreat_time(dateTime);
        Object proceed = null;
        try {
            //执行任务
            long startTime = System.currentTimeMillis();
            proceed = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            log.info("方法: " + joinPoint.getSignature().getName() + " 执行时间:" + (endTime - startTime) + "毫秒");
            systemLog.setCost_time(endTime - startTime);
            systemLog.setRet(proceed.toString());
            return proceed;
        }catch (Throwable e){
            String exception = e.toString();
            systemLog.setException(exception);
            if(!e.getClass().equals(BusinessException.class)){
                //如果是BusinessException的话直接抛原异常
                throw new RuntimeException(e);
            }
            BusinessException businessException = (BusinessException) e;
            throw businessException;
        }finally {
            //日志信息存入数据库
            addOperationLog(joinPoint,systemLog);
        }
    }

    private void addOperationLog(ProceedingJoinPoint joinPoint, Log systemLog) {
        HttpServletRequest request = HttpUtil.gerRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SystemLog annotation = method.getAnnotation(SystemLog.class);
        String businessName = annotation.businessName();
        // 打印描述信息
        log.info("BusinessName   : {}", businessName);
        systemLog.setBusinessName(businessName);
        // 打印请求 URL
        log.info("URL            : {}", request.getRequestURL());
        systemLog.setURL(request.getRequestURL().toString());
        // 打印 Http method
        log.info("HTTP Method    : {}", request.getMethod());
        systemLog.setHttpMethod(request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        systemLog.setTypeName(joinPoint.getSignature().getDeclaringTypeName());
        systemLog.setMethodName(joinPoint.getSignature().getName());
        // 打印请求的 IP
        log.info("IP             : {}", request.getRemoteHost());
        systemLog.setRemoteHost(request.getRemoteHost());
        // 打印请求入参
        log.info("Request Args   : {}", JSONObject.toJSONString(joinPoint.getArgs()));
        systemLog.setArgs(JSONObject.toJSONString(joinPoint.getArgs()));
        //异步日志入库
        asyncTaskExecutor.execute(new SaveSystemLogThread(systemLog,logService));
    }
    @AllArgsConstructor
    private static class SaveSystemLogThread implements Runnable{
        private Log log;
        private LogService logService;
        @Override
        public void run() {
            try {
                //休眠一秒再插入
                Thread.sleep(1000);
                logService.insertLog(log);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
