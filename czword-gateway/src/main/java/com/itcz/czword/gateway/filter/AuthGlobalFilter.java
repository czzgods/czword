package com.itcz.czword.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.itcz.czword.common.utils.JwtUtil;
import com.itcz.czword.model.common.ResultUtils;
import com.itcz.czword.model.constant.UserConstant;
import com.itcz.czword.model.enums.ErrorCode;
import com.itcz.czword.model.vo.common.ResponseResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            //获取request对象
            ServerHttpRequest request = exchange.getRequest();
            //获取请求地址
            String path = request.getURI().getPath();
            log.info("path {}", path);
            if (path.contains("login")) {
                //表示当前操作的登录业务,直接放行
                return chain.filter(exchange);
            }
            if (path.contains("api")) {
                //表明当前操作的是接口调用
                String czword = request.getHeaders().get("czword").get(0);
                if (czword != null) {
                    return chain.filter(exchange);
                }
                return out(exchange.getResponse(), ErrorCode.PARAMS_ERROR);
            }
            //如果路径都不包含上面那两个条件，就走下面这个路径
            //先获取用户登录的token
            String token = request.getHeaders().get("token").get(0);
            //解析token
            String userId = JwtUtil.parseJWT(token).getSubject();
            //查询redis中是否存入用户数据
            Boolean hasKey = redisTemplate.hasKey(UserConstant.USER_LOGIN_STATE + userId);
            if(hasKey){
                //存在说明用户已经登录，直接放行
                return chain.filter(exchange);
            }else {
                return out(exchange.getResponse(),ErrorCode.NOT_LOGIN_ERROR);
            }
        }catch (Exception e) {
            e.printStackTrace();
            return out(exchange.getResponse(),ErrorCode.NOT_LOGIN_ERROR);
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
    private Mono<Void> out(ServerHttpResponse response, ErrorCode errorCode) {
        ResponseResult result = ResponseResult.result(null, errorCode);
        byte[] bits = JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }
}
