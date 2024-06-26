package com.itcz.czword.interfaces.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itcz.czword.common.service.exception.BusinessException;
import com.itcz.czword.interfaces.mapper.InterfaceMapper;
import com.itcz.czword.interfaces.mapper.UserInterfaceInfoMapper;
import com.itcz.czword.interfaces.service.UserInterfaceInfoService;
import com.itcz.czword.model.dto.interfaces.InterfaceAssign;
import com.itcz.czword.model.entity.interfaces.Interface;
import com.itcz.czword.model.entity.interfaces.UserInterfaceInfo;
import com.itcz.czword.model.enums.ErrorCode;
import com.itcz.czword.model.vo.interfaces.InterfaceVo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 李钟意
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
* @createDate 2024-03-28 15:00:27
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService {
    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;
    @Resource
    private InterfaceMapper interfaceMapper;
    @Override
    public Boolean doAssign(InterfaceAssign interfaceAssign) {
        Long userId = interfaceAssign.getUserId();
        Long interfaceId = interfaceAssign.getInterfaceId();
        Long useCount = interfaceAssign.getUseCount();
        if(userId <= 0 || interfaceId <= 0 || useCount<=0 || useCount>=9999){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //查询数据库是否存在数据
        LambdaQueryWrapper<UserInterfaceInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInterfaceInfo::getUserId,userId);
        queryWrapper.eq(UserInterfaceInfo::getInterfaceInfoId,interfaceId);
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoMapper.selectOne(queryWrapper);
        if(userInterfaceInfo != null){
            //说明已经存在数据，此时就是在原有的调用次数上新增准备分配过去的调用次数就行了
            userInterfaceInfo.setLeftNum(userInterfaceInfo.getLeftNum().intValue() + interfaceAssign.getUseCount().intValue());
            userInterfaceInfoMapper.updateById(userInterfaceInfo);
            return true;
        }
        //到这里说明是本来就不存在数据的
        UserInterfaceInfo newUserInterfaceInfo = new UserInterfaceInfo();
        newUserInterfaceInfo.setUserId(userId);
        newUserInterfaceInfo.setInterfaceInfoId(interfaceId);
        newUserInterfaceInfo.setLeftNum(useCount.intValue());
        userInterfaceInfoMapper.insert(newUserInterfaceInfo);
        return true;
    }

    @Override
    public List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int i) {
        return userInterfaceInfoMapper.listTopInvokeInterfaceInfo(i);
    }

    @Override
    public InterfaceVo allCount() {
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoMapper.allCount();
        Long interfaceInfoId = userInterfaceInfo.getInterfaceInfoId();
        Integer totalNum = userInterfaceInfo.getTotalNum();
        LambdaQueryWrapper<Interface> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Interface::getId,interfaceInfoId);
        Interface anInterface = interfaceMapper.selectOne(queryWrapper);
        InterfaceVo interfaceVo = new InterfaceVo();
        BeanUtils.copyProperties(anInterface,interfaceVo);
        interfaceVo.setTotalNum(totalNum);
        return interfaceVo;
    }
}




