package com.itcz.czword.common.service.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class FeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attr=(ServletRequestAttributes) requestAttributes;
        //获取原请求信息
        HttpServletRequest request = attr.getRequest();
        String token = request.getHeader("token");
        if(StringUtils.hasText(token)){
            requestTemplate.header("token",token);
        }
    }
}
