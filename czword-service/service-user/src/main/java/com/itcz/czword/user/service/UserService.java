package com.itcz.czword.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itcz.czword.model.common.PageRequest;
import com.itcz.czword.model.dto.email.EmailBindingDto;
import com.itcz.czword.model.dto.user.LoginAccountDto;
import com.itcz.czword.model.dto.user.LoginByEmailDto;
import com.itcz.czword.model.dto.user.UserDeleteDto;
import com.itcz.czword.model.entity.user.User;
import com.itcz.czword.model.vo.user.LoginUserVo;
import com.itcz.czword.model.vo.user.LoginVo;
import com.itcz.czword.model.vo.user.UserVo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @param userName
     * @return
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String userName);

    /**
     * 账户邮箱绑定
     * @param emailBindingDto
     */
    void bindingEmail(EmailBindingDto emailBindingDto,HttpServletRequest httpServletRequest);

    /**
     * 使用账号密码登录
     * @param loginAccountDto
     * @return
     */
    LoginVo login(LoginAccountDto loginAccountDto);

    /**
     * 使用邮箱登录
     * @param loginByEmailDto
     * @return
     */
    LoginVo loginByEmail(LoginByEmailDto loginByEmailDto);

    /**
     * 用户退出登录
     */
    Boolean userLogout(HttpServletRequest httpServletRequest);

    /**
     * 获取当前登录用户
     * @return
     */
    LoginUserVo getLoginUser(HttpServletRequest httpServletRequest);

    /**
     * 管理员删除用户
     * @param userDeleteDto
     * @return
     */
    Boolean deleteUser(UserDeleteDto userDeleteDto);

    /**
     * 用户注销账号
     * @return
     */
    Boolean userLogOff(HttpServletRequest httpServletRequest);

    /**
     * 分页查询用户
     * @param pageRequest
     * @return
     */
    Page<UserVo> listUser(PageRequest pageRequest);

    /**
     * 获取随机毒鸡汤
     * @return
     */
    String getRandomWord();

    /**
     * 用户上传头像
     * @param multipartFile
     * @return
     */
    String upload(MultipartFile multipartFile);
}
