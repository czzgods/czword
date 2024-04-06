package com.itcz.czword.user.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.itcz.czword.model.constant.UserConstant;
import com.itcz.czword.model.vo.common.ValidateCodeVo;
import com.itcz.czword.user.service.ValidateCodeService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    @Override
    public ValidateCodeVo generateValidateCode() {
        //使用hutool工具包生成图像验证码
        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(150, 48, 4, 20);
        String codeValue = circleCaptcha.getCode(); //生成的4位验证码
        String imageBase64 = circleCaptcha.getImageBase64(); //生成的图片验证码，里面包含了4位验证码
        //使用UUID生成唯一id作为图像验证码的id存入redis
        String codeKey = UUID.randomUUID().toString().replace("-", "");
        //将生成好的图像验证码存入redis
        redisTemplate.opsForValue().set(UserConstant.VALIDATE_CODE_KEY+codeKey,codeValue,5, TimeUnit.MINUTES);
        //封装数据并返回
        ValidateCodeVo validateCodeVo = new ValidateCodeVo();
        validateCodeVo.setCodeKey(codeKey);
        validateCodeVo.setCodeValue("data:image/png;base64," + imageBase64);
        return validateCodeVo;
    }
}
