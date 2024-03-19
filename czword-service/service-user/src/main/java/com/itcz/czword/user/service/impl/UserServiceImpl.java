package com.itcz.czword.user.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itcz.common.service.exception.BusinessException;
import com.itcz.czword.model.enums.ErrorCode;
import com.itcz.czword.user.mapper.UserMapper;
import com.itcz.czword.model.entity.user.User;
import com.itcz.czword.user.service.UserService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
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
}




