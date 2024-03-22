package com.itcz.czword.user.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.itcz.czword.common.service.exception.BusinessException;
import com.itcz.czword.model.constant.UserConstant;
import com.itcz.czword.user.properties.EmailProperties;
import com.itcz.czword.user.service.EmailService;
import jakarta.annotation.Resource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class EmailServiceImpl implements EmailService {
    @Resource
    private JavaMailSender mailSender;
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private EmailProperties emailProperties;
    @Override
    public Boolean sendRegisterEmail(String email) {
        //创建锁对象
        RLock lock = redissonClient.getLock(UserConstant.EMAIL_SEND_LOCK + email);
        try {
            //尝试获取锁
            if(lock.tryLock(0,30000,TimeUnit.MILLISECONDS)) {
                SimpleMailMessage message = new SimpleMailMessage();
                //设置验证码的标题
                message.setSubject("文心一言毒鸡汤平台验证码");
                //需要发送的验证码
                String code = randomCode();
                //验证码存入redis
                redisTemplate.opsForValue().set(UserConstant.EMAIL_SEND_CODE+email, code, 1, TimeUnit.MINUTES);
                //设置发送验证码的内容
                message.setText("您收到的验证码为：" + code + ",有效时间为1分钟,请尽快验证");
                //设置邮箱发送给谁
                message.setTo(email);
                //设置邮箱信件的来源
                message.setFrom(emailProperties.getUsername());
                //发送邮箱
                mailSender.send(message);
                return true;
            }
        } catch (Exception e){
            BusinessException businessException = (BusinessException) e;
            throw businessException;
        }finally {
            //首先判断当前锁是否是该方法的线程id，避免释放掉其它线程的锁
            if(lock.isHeldByCurrentThread())
                lock.unlock();
        }
        return false;
    }

    @Override
    public boolean sendEmail(String email) {
        return this.sendRegisterEmail(email);
    }

    /**
     * 随机四位数验证码
     * @return
     */
    private String randomCode() {
        String code = RandomUtil.randomNumbers(4);
        return code;
    }
}
