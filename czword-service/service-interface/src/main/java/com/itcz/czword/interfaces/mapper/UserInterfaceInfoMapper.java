package com.itcz.czword.interfaces.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itcz.czword.model.entity.interfaces.UserInterfaceInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 李钟意
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2024-03-28 15:00:27
* @Entity generator.domain.UserInterfaceInfo
*/
@Mapper
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int i);
}




