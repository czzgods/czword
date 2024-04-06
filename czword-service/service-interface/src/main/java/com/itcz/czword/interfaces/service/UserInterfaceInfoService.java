package com.itcz.czword.interfaces.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itcz.czword.model.dto.interfaces.InterfaceAssign;
import com.itcz.czword.model.entity.interfaces.UserInterfaceInfo;

import java.util.List;

/**
* @author 李钟意
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2024-03-28 15:00:27
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 为用户分配使用次数
     * @param interfaceAssign
     * @return
     */
    Boolean doAssign(InterfaceAssign interfaceAssign);

    /**
     * 查询调用前三的接口
     * @param i
     * @return
     */
    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int i);
}
