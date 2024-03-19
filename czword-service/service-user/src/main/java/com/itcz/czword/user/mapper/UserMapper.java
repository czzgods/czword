package com.itcz.czword.user.mapper;

import com.itcz.czword.model.entity.user.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
* @author 李钟意
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2024-03-19 10:16:36
* @Entity generator.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




