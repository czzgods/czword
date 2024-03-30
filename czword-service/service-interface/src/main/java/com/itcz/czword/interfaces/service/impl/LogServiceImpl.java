package com.itcz.czword.interfaces.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itcz.czword.common.service.commonService.LogService;
import com.itcz.czword.interfaces.mapper.LogMapper;
import com.itcz.czword.model.entity.log.Log;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
* @author 李钟意
* @description 针对表【log(操作日志表)】的数据库操作Service实现
* @createDate 2024-03-26 15:38:40
*/

@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log>
    implements LogService {
    @Resource
    private LogMapper logMapper;
    @Override
    public void insertLog(Log log) {
        logMapper.insert(log);
    }
}





