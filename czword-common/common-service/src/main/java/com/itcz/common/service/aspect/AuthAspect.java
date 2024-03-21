package com.itcz.common.service.aspect;


import com.alibaba.fastjson.JSON;
import com.itcz.common.service.annotation.AuthCheck;
import com.itcz.common.service.exception.BusinessException;
import com.itcz.czword.model.common.ResultUtils;
import com.itcz.czword.model.constant.UserConstant;
import com.itcz.czword.model.entity.user.User;
import com.itcz.czword.model.enums.ErrorCode;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 权限校验切面
 */
@Component
@Aspect
@Slf4j
public class AuthAspect {
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    @Pointcut("@annotation(com.itcz.common.service.annotation.AuthCheck)")
    public void pt(){}

    @Around("pt()")
    public Object authCheck(ProceedingJoinPoint joinPoint){
        log.error("权限校验切面启动！！！");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        AuthCheck annotation = method.getAnnotation(AuthCheck.class);
        String mustRole = annotation.mustRole();
        //String mustRole = authCheck.mustRole();
        String jsonStr = redisTemplate.opsForValue().get(UserConstant.USER_LOGIN_STATE);
        User user = JSON.parseObject(jsonStr, User.class);
        if (user != null) {
            String userRole = user.getUserRole();
            if (mustRole.equals(userRole)) {
                try {
                    //放行
                    return joinPoint.proceed();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        }else {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return ResultUtils.error(ErrorCode.NO_AUTH_ERROR);
    }
}
