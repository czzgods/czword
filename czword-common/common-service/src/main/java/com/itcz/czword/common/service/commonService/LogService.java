package com.itcz.czword.common.service.commonService;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itcz.czword.model.entity.log.Log;

/**
* @author 李钟意
* @description 针对表【log(操作日志表)】的数据库操作Service
* @createDate 2024-03-26 15:38:41
*/
public interface LogService  {

    void insertLog(Log log);
}
