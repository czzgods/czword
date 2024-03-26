package com.itcz.czword.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itcz.czword.model.entity.log.Log;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 李钟意
* @description 针对表【log(操作日志表)】的数据库操作Mapper
* @createDate 2024-03-26 15:38:40
* @Entity generator.domain.Log
*/
@Mapper
public interface LogMapper extends BaseMapper<Log> {

}




