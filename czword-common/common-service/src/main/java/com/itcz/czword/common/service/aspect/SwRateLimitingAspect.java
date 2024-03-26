package com.itcz.czword.common.service.aspect;

import com.itcz.czword.common.service.annotation.SwRateLimiting;
import com.itcz.czword.common.service.exception.BusinessException;
import com.itcz.czword.common.utils.HttpUtil;
import com.itcz.czword.model.constant.RateLimitConstant;
import com.itcz.czword.model.enums.ErrorCode;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 滑动窗口限流切面
 */
@Component
@Aspect
@Order(2)
@Slf4j
public class SwRateLimitingAspect {
    @Resource
    private RedisTemplate redisTemplate;
    @Pointcut("@annotation(com.itcz.czword.common.service.annotation.SwRateLimiting)")
    public void pt(){}

    @Around("pt()")
    public Object startRateLimiting(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取注解参数
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SwRateLimiting rateLimiting = method.getAnnotation(SwRateLimiting.class);
        long time = rateLimiting.time();
        long limitCount = rateLimiting.count();
        HttpServletRequest request = HttpUtil.gerRequest();
        //请求ip
        String ip = request.getRemoteAddr();
        //获取请求uri
        String uri = request.getRequestURI();
        //拼接redis的key
        String key = RateLimitConstant.RATE_LIMITING_KEY.concat(ip).concat(uri);
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        //查询访问次数
        Long count = zSetOperations.zCard(key);
        if(count >= limitCount){
            throw new BusinessException(ErrorCode.RATE_LIMITING_ERROR);
        }
        //获取当前时间
        long currentMs = System.currentTimeMillis();
        //添加当前时间
        zSetOperations.add(key,currentMs,currentMs);
        //设置过期时间
        redisTemplate.expire(key,time, TimeUnit.SECONDS);
        //删除窗口以外的值
        zSetOperations.removeRangeByScore(key,0,currentMs - time * 1000);
        //放行
        return joinPoint.proceed();
    }
}
