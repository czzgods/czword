package com.itcz.czword.interfaces.controller;


import com.itcz.czword.common.service.annotation.AuthCheck;
import com.itcz.czword.interfaces.service.InterfaceService;
import com.itcz.czword.model.common.BaseResponse;
import com.itcz.czword.model.common.ResultUtils;
import com.itcz.czword.model.constant.UserConstant;
import com.itcz.czword.model.dto.interfaces.InterfaceDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
