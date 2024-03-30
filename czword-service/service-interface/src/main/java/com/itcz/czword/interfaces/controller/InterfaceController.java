package com.itcz.czword.interfaces.controller;


import com.itcz.czword.common.service.annotation.AuthCheck;
import com.itcz.czword.common.service.exception.BusinessException;
import com.itcz.czword.common.utils.UserContextUtil;
import com.itcz.czword.interfaces.service.InterfaceService;
import com.itcz.czword.model.common.BaseResponse;
import com.itcz.czword.model.common.ResultUtils;
import com.itcz.czword.model.constant.UserConstant;
import com.itcz.czword.model.dto.interfaces.InterfaceDto;
import com.itcz.czword.model.dto.interfaces.InterfaceRequest;
import com.itcz.czword.model.entity.user.User;
import com.itcz.czword.model.enums.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@Tag(name = "接口控制层")
@RestController
@RequestMapping("/interface")
public class InterfaceController {
    @Resource
    private InterfaceService interfaceService;
    @Operation(summary = "新增接口")
    @PostMapping("/addInterface")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse addInterface(@RequestBody InterfaceDto interfaceDto){
        interfaceService.addInterface(interfaceDto);
        return ResultUtils.success("接口信息添加成功");
    }
    @Operation(summary = "随机毒鸡汤")
    @PostMapping("/invoke")
    public String invoke(@RequestBody InterfaceRequest request){
        Long interfaceId = request.getInterfaceId();
        if(interfaceId <= 0){
            throw new BusinessException(ErrorCode.INTERFACE_NOT_EXIST);
        }
        User user = UserContextUtil.getUser();
        String sentence = interfaceService.invoke(user,interfaceId);
        return sentence;
    }
}
