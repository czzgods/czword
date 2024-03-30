package com.itcz.czword.common.utils;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;

import java.nio.charset.StandardCharsets;

public class HttpRequestUtil {
    //在请求头中添加校验信息
    public static final String REQUEST_HEADER = "czword";
    public static final String HTTP_REQUEST_URL_HOST = "http://localhost:8084";

    //无参GET
    public static HttpResponse requestGet(String url,String header){
        HttpResponse execute = HttpUtil.createGet(HTTP_REQUEST_URL_HOST + url)
                .charset(StandardCharsets.UTF_8)
                .header(REQUEST_HEADER, header)
                .execute();
        return execute;
    }
    //有参GET
    public static HttpResponse requestGet(String url,String header,String pathVariable){
        HttpResponse execute = HttpUtil.createGet(HTTP_REQUEST_URL_HOST + url + "/" + pathVariable)
                .charset(StandardCharsets.UTF_8)
                .header(REQUEST_HEADER, header)
                .execute();
        return execute;
    }
    //POST
    public static HttpResponse requestPost(String url,String header,String body){
        HttpResponse execute = HttpUtil.createPost(HTTP_REQUEST_URL_HOST + url)
                .charset(StandardCharsets.UTF_8)
                .header(REQUEST_HEADER, header)
                .body(body)
                .execute();
        return execute;
    }
}
