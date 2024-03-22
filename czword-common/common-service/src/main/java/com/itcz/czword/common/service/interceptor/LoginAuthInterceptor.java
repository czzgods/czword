package com.itcz.czword.common.service.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.itcz.czword.common.service.exception.BusinessException;
import com.itcz.czword.common.utils.JwtUtil;
import com.itcz.czword.common.utils.UserContextUtil;
import com.itcz.czword.model.constant.UserConstant;
import com.itcz.czword.model.entity.user.User;
import com.itcz.czword.model.enums.ErrorCode;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

@Component
public class LoginAuthInterceptor implements HandlerInterceptor {
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    /**
     * 前置方法，可用于拦截请求
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求方式
        String method = request.getMethod();
        if("OPTIONS".equals(method)) {      // 如果是跨域预检请求，直接放行
            return true ;
        }
        //获取用户登录携带的token
        String token = request.getHeader("token");
        if(StrUtil.isEmpty(token)){
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        String userId = JwtUtil.parseJWT(token).getSubject();
        String userInfoJSON = redisTemplate.opsForValue().get(UserConstant.USER_LOGIN_STATE + userId);
        if(StrUtil.isEmpty(userInfoJSON)){
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        //将用户数据存入threadLocal
        User user = JSON.parseObject(userInfoJSON, User.class);
        UserContextUtil.setUser(user);
        //重置redis的用户信息过期时间
        redisTemplate.expire(UserConstant.USER_LOGIN_STATE+token,30, TimeUnit.MINUTES);
        //放行
        return true;
    }
    /**
     * 后置方法，所有操作结束执行
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        // 移除threadLocal中的数据
        UserContextUtil.removeUser();
    }
}
