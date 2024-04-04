package com.itcz.czword.gateway.filter;

import com.itcz.czword.common.service.exception.BusinessException;
import com.itcz.czword.model.enums.ErrorCode;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AskGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        if(!path.contains("/api")){
            //如果不是api接口请求则直接放行
            return chain.filter(exchange);
        }
        String czword = request.getHeaders().get("czword").get(0);
        if(!StringUtils.hasText(czword)){
            //如果不存在该请求头 则抛异常
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
