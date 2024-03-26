package com.itcz.czword.common.service.aspect;


import com.alibaba.fastjson.JSON;
import com.itcz.czword.common.service.annotation.AuthCheck;
import com.itcz.czword.common.service.exception.BusinessException;
import com.itcz.czword.common.utils.HttpUtil;
import com.itcz.czword.common.utils.JwtUtil;
import com.itcz.czword.common.utils.UserContextUtil;
import com.itcz.czword.model.constant.UserConstant;
import com.itcz.czword.model.entity.user.User;
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
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

/**
 * 权限校验切面
 */
@Component
@Aspect
@Order(1)
@Slf4j
public class AuthAspect {
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    @Pointcut("@annotation(com.itcz.czword.common.service.annotation.AuthCheck)")
    public void pt(){}

    @Around("pt()")
    public Object authCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        log.error("权限校验切面启动！！！");
        /*HttpServletRequest httpServletRequest = HttpUtil.gerRequest();
        String token = httpServletRequest.getHeader("token");
        String userId = JwtUtil.parseJWT(token).getSubject();*/
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        AuthCheck annotation = method.getAnnotation(AuthCheck.class);
        String mustRole = annotation.mustRole();
        //String mustRole = authCheck.mustRole();
        /*String jsonStr = redisTemplate.opsForValue().get(UserConstant.USER_LOGIN_STATE+userId);
        User user = JSON.parseObject(jsonStr, User.class);*/
        User user = UserContextUtil.getUser();
        if(user == null){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        String userRole = user.getUserRole();
        if (!mustRole.equals(userRole)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //放行
        return joinPoint.proceed();
    }
}
