package com.itcz.czword.model.vo.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.itcz.czword.model.enums.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Schema(description = "响应结果实体类")
public class ResponseResult<T> implements Serializable {

    //状态码
    @Schema(description = "业务状态码")
    private Integer code;

    //响应消息
    @Schema(description = "响应消息")
    private String message;

    //返回数据
    @Schema(description = "业务数据")
    private T data;

    // 私有化构造
    private ResponseResult() {}

    public static <T> ResponseResult<T> result(T body, Integer code, String message) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setData(body);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static <T> ResponseResult<T> result(T body, ErrorCode errorCode){
        return  result(body,errorCode.getCode(),errorCode.getMessage());
    }

}
