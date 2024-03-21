package com.itcz.czword.user.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itcz.common.service.exception.BusinessException;
import com.itcz.common.utils.JwtUtil;
import com.itcz.common.utils.UserContextUtil;
import com.itcz.czword.model.constant.UserConstant;
import com.itcz.czword.model.dto.email.EmailBindingDto;
import com.itcz.czword.model.dto.user.LoginAccountDto;
import com.itcz.czword.model.dto.user.LoginByEmailDto;
import com.itcz.czword.model.enums.ErrorCode;
import com.itcz.czword.model.vo.user.LoginUserVo;
import com.itcz.czword.model.vo.user.LoginVo;
import com.itcz.czword.user.mapper.UserMapper;
import com.itcz.czword.model.entity.user.User;
import com.itcz.czword.user.service.UserService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;


/**
* @author 李钟意
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-03-19 10:16:36
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "cz";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String userName) {
        if(!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次输入密码不一致");
        }
        //加锁
        synchronized (userAccount.intern()) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUserAccount, userAccount);
            Long count = userMapper.selectCount(queryWrapper);
            if(count > 0){
                throw new BusinessException(ErrorCode.ACCOUNT_EXIST);
            }
            //密码加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            //给用户分配 ak/sk
            String accessKey = DigestUtil.md5Hex(SALT + userAccount + RandomUtil.randomNumbers(5));
            String secretKey = DigestUtil.md5Hex(SALT + userAccount + RandomUtil.randomNumbers(8));
            //插入数据
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserName(StringUtils.upperCase(userAccount));//该方法可以将小写字母转换成大写
            user.setUserPassword(encryptPassword);
            user.setAccessKey(accessKey);
            user.setSecretKey(secretKey);
            boolean save = this.save(user);
            if(!save){
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"账号注册失败");
            }
            return user.getId();
        }
    }

    @Override
    public void bindingEmail(EmailBindingDto emailBindingDto) {
        //获取当前登录用户
        String jsonString = redisTemplate.opsForValue().get(UserConstant.USER_LOGIN_STATE);
        User user = JSON.parseObject(jsonString, User.class);
        if(user == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        String email = emailBindingDto.getEmail();
        String code = emailBindingDto.getCode();
        String emailCode = redisTemplate.opsForValue().get(UserConstant.EMAIL_SEND_CODE + email);
        if(StringUtils.isBlank(emailCode)){
            throw new BusinessException(ErrorCode.EMAIL_CODE_NOT_EXIT);
        }
        if(!emailCode.equals(code)){
            throw new BusinessException(ErrorCode.EMAIL_CODE_ERROR);
        }
        user.setEmail(email);
        this.updateById(user);
        //绑定成功，删除验证码
        redisTemplate.delete(UserConstant.EMAIL_SEND_CODE + email);
    }

    @Override
    public LoginVo login(LoginAccountDto loginAccountDto) {
        if(loginAccountDto == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = loginAccountDto.getUserAccount();
        //查询数据库
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount,userAccount);
        User user = userMapper.selectOne(queryWrapper);
        if(user == null){
            throw new BusinessException(ErrorCode.ACCOUNT_NOT_EXIST);
        }
        String userPassword = loginAccountDto.getUserPassword();
        //密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //获取存入数据库值的密码
        String dbPassword = user.getUserPassword();
        if(!encryptPassword.equals(dbPassword)){
            throw new BusinessException(ErrorCode.PASSWORD_ERROR);
        }
        //到这里说明账号密码正确。生成JWT令牌
        redisTemplate.opsForValue().set(UserConstant.USER_LOGIN_STATE, JSON.toJSONString(user));
        String token = JwtUtil.createJWT(user.getId().toString());
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        return loginVo;
    }

    @Override
    public LoginVo loginByEmail(LoginByEmailDto loginByEmailDto) {
        if(loginByEmailDto == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String email = loginByEmailDto.getEmail();
        //查询数据库
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email);
        User user = userMapper.selectOne(queryWrapper);
        if(user == null){
            throw new BusinessException(ErrorCode.ACCOUNT_NOT_EXIST);
        }
        String code = loginByEmailDto.getCode();
        //从redis中查询发送的验证码
        String redisCode = redisTemplate.opsForValue().get(UserConstant.EMAIL_SEND_CODE + email);
        if(!code.equals(redisCode)){
            throw new BusinessException(ErrorCode.EMAIL_CODE_ERROR);
        }
        //到这里说明验证码正确
        redisTemplate.opsForValue().set(UserConstant.USER_LOGIN_STATE, JSON.toJSONString(user));
        //删除验证码
        redisTemplate.delete(UserConstant.EMAIL_SEND_CODE + email);
        //生成JWT令牌
        String token = JwtUtil.createJWT(user.getId().toString());
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        return loginVo;
    }

    @Override
    public void userLogout() {
        //删除缓存中的用户信息
        redisTemplate.delete(UserConstant.USER_LOGIN_STATE);
    }

    @Override
    public LoginUserVo getLoginUser() {
        //获取存入redis中的用户信息
        String jsonStr = redisTemplate.opsForValue().get(UserConstant.USER_LOGIN_STATE);
        //类型转换
        User user = JSON.parseObject(jsonStr, User.class);
        if(user == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        //类型转换
        LoginUserVo loginUserVo = new LoginUserVo();
        BeanUtils.copyProperties(user,loginUserVo);
        return loginUserVo;
    }
}




