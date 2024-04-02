package com.itcz.czword.interfaces.service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itcz.czword.model.common.PageRequest;
import com.itcz.czword.model.dto.interfaces.InterfaceDto;
import com.itcz.czword.model.dto.interfaces.InterfaceRequest;
import com.itcz.czword.model.entity.interfaces.Interface;
import com.itcz.czword.model.entity.user.User;
import com.itcz.czword.model.vo.interfaces.InterfaceVo;

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

    /**
     * 分页获取接口信息
     * @param pageRequest
     * @return
     */
    Page<InterfaceVo> listInterfaces(PageRequest pageRequest);

    /**
     * 接口上线
     * @param request
     * @return
     */
    Boolean online(InterfaceRequest request);

    /**
     * 接口下线
     * @param request
     * @return
     */
    Boolean outline(InterfaceRequest request);
}
