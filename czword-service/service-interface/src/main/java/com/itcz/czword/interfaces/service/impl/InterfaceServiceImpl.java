package com.itcz.czword.interfaces.service.impl;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itcz.czword.common.service.exception.BusinessException;
import com.itcz.czword.common.utils.AkSkUtils;
import com.itcz.czword.common.utils.HttpRequestUtil;
import com.itcz.czword.interfaces.service.InterfaceService;
import com.itcz.czword.model.dto.interfaces.InterfaceDto;
import com.itcz.czword.model.entity.interfaces.Interface;
import com.itcz.czword.model.entity.user.User;
import com.itcz.czword.model.enums.ErrorCode;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.itcz.czword.interfaces.mapper.InterfaceMapper;

import java.util.concurrent.TimeUnit;

/**
* @author 李钟意
* @description 针对表【interface(接口信息)】的数据库操作Service实现
* @createDate 2024-03-28 15:00:23
*/
@Service
public class InterfaceServiceImpl extends ServiceImpl<InterfaceMapper, Interface>
        implements InterfaceService {
    @Resource
    private InterfaceMapper interfaceMapper;
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    private static final String INTERFACE_INFO="interface:info";
    @Override
    public void addInterface(InterfaceDto interfaceDto) {
        if(interfaceDto == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String url = interfaceDto.getUrl();
        if(StringUtils.isBlank(url)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaQueryWrapper<Interface> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Interface::getUrl,url);
        Interface anInterface = interfaceMapper.selectOne(queryWrapper);
        if(anInterface != null){
            throw new BusinessException(ErrorCode.INTERFACE_EXIST);
        }
        Interface interfaces = new Interface();
        BeanUtils.copyProperties(interfaceDto,interfaces);
        interfaceMapper.insert(interfaces);
    }

    @Override
    public String invoke(User user,Long interfaceId) {
        //构建请求头信息
        String header = AkSkUtils.setHeaders(user);
        Interface interfaces = null;
        //查询redis中是否存在接口
        String interfaceInfoStr = redisTemplate.opsForValue().get(INTERFACE_INFO);
        if(StringUtils.isBlank(interfaceInfoStr)){
            //如果redis中不存在,则数据库中查询
           interfaces = interfaceMapper.selectById(interfaceId);
           if(interfaces == null){
               //防止缓存击穿，缓存空数据
               redisTemplate.opsForValue().set(INTERFACE_INFO,null,3, TimeUnit.MINUTES);
           }
            //存入redis
            redisTemplate.opsForValue().set(INTERFACE_INFO, JSON.toJSONString(interfaces));
        }else {
            interfaces = JSON.parseObject(interfaceInfoStr, Interface.class);
        }
        if(interfaces == null){
            throw new BusinessException(ErrorCode.INTERFACE_NOT_EXIST);
        }
        String url = interfaces.getUrl();
        String method = interfaces.getMethod();
        //调用接口，获取返回结果
        HttpResponse response = null;
        String json = JSON.toJSONString(user);
        if("POST".equals(method)){
            response = HttpRequestUtil.requestPost(url, header, json);
        }else if ("GET".equals(method)){
            response = HttpRequestUtil.requestGet(url,header);
        }else {
            throw new BusinessException(ErrorCode.REQUEST_METHOD_ERROR);
        }
        String sentence = response.body();
        return sentence;
    }
}




