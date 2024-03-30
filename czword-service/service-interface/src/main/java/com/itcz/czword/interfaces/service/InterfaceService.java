package com.itcz.czword.interfaces.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itcz.czword.model.dto.interfaces.InterfaceDto;
import com.itcz.czword.model.entity.interfaces.Interface;
import com.itcz.czword.model.entity.user.User;

/**
* @author 李钟意
* @description 针对表【interface(接口信息)】的数据库操作Service
* @createDate 2024-03-28 15:00:23
*/
public interface InterfaceService extends IService<Interface> {
    /**
     * 新增接口
     * @param interfaceDto
     * @return
     */
    void addInterface(InterfaceDto interfaceDto);

    /**
     * 接口调用
     * @param interfaceId
     * @return
     */
    String invoke(User user,Long interfaceId);

}
