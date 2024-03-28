package com.itcz.czword.interfaces.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itcz.czword.interfaces.mapper.UserInterfaceInfoMapper;
import com.itcz.czword.interfaces.service.UserInterfaceInfoService;
import com.itcz.czword.model.entity.interfaces.UserInterfaceInfo;
import org.springframework.stereotype.Service;

/**
* @author 李钟意
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
* @createDate 2024-03-28 15:00:27
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService {

}




