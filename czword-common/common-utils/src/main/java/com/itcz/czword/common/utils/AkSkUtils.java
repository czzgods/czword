package com.itcz.czword.common.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.itcz.czword.model.entity.user.User;

import java.util.HashMap;
import java.util.Map;

/**
 *在请求头中加密ak,sk
 */
public class AkSkUtils {
    public static String setHeaders(User user){
        //创建一个map集合去封装请求头数据
        Map<String,String> map = new HashMap<>();
        String userId = user.getId().toString();
        map.put("body",userId);
        String accessKey = user.getAccessKey();
        map.put("accessKey",accessKey);
        //随机数
        String nonce = RandomUtil.randomNumbers(4);
        map.put("nonce",nonce);
        //时间戳
        Long time = System.currentTimeMillis()/1000;
        String timestamp = time.toString();
        map.put("timestamp",timestamp);
        //加密秘钥
        String sign = generationSign(map.toString(), user.getSecretKey());
        map.put("sign",sign);
        String result = JSON.toJSONString(map);
        return result;
    }

    public static String generationSign(String map, String secretKey) {
        //秘钥加密
        String sign = DigestUtil.md5Hex16(map + secretKey);
        return sign;
    }
}
