package com.itcz.czword.common.service.interceptor;

import com.alibaba.fastjson2.JSON;
import com.itcz.czword.common.service.exception.BusinessException;
import com.itcz.czword.common.utils.AkSkUtils;
import com.itcz.czword.model.constant.UserConstant;
import com.itcz.czword.model.entity.user.User;
import com.itcz.czword.model.enums.ErrorCode;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

public class InterfaceApiInterceptor implements HandlerInterceptor {
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String czword = request.getHeader("czword");
        //因为在设置请求头时用的是map集合转换成的字符串，所以现在要转换回来
        Map<String,String> map = JSON.parseObject(czword, Map.class);
        if(map == null){
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        //获取并删除，因为在设置请求头中的sk时没有这个，所以这里删除了方便后续验证sk
        String sign = map.remove("sign");
        Long lastTimeStamp = Long.valueOf(map.get("timestamp"));
        Long nowTimeStamp = System.currentTimeMillis()/1000;
        if((nowTimeStamp - lastTimeStamp) >= 5){
            //检测时间戳是否大于5秒
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        Long userId = Long.valueOf(map.get("body"));
        //从redis中查询当然用户数据
        String userStr = redisTemplate.opsForValue().get(UserConstant.USER_LOGIN_STATE + userId);
        //类型转换
        User user = JSON.parseObject(userStr, User.class);
        if(user == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        String accessKey = map.get("accessKey");
        String userAccessKey = user.getAccessKey();
        if(!accessKey.equals(userAccessKey)){
            throw new BusinessException(ErrorCode.AK_ERROR);
        }
        String secretKey = user.getSecretKey();
        //加密sk，与请求头中的传递过来的sk做比较
        String generationSign = AkSkUtils.generationSign(map.toString(), secretKey);
        if(!generationSign.equals(sign)){
            //这里说明秘钥不正确
            throw new BusinessException(ErrorCode.SK_ERROR);
        }
        //放行
        return true;
    }
}
