package com.itcz.czword.interfaces.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itcz.czword.common.service.exception.BusinessException;
import com.itcz.czword.interfaces.service.InterfaceService;
import com.itcz.czword.model.dto.interfaces.InterfaceDto;
import com.itcz.czword.model.entity.interfaces.Interface;
import com.itcz.czword.model.enums.ErrorCode;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.itcz.czword.interfaces.mapper.InterfaceMapper;
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
}




