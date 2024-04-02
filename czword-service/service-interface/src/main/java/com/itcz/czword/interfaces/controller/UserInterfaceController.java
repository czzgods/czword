package com.itcz.czword.interfaces.controller;

import com.itcz.czword.common.service.exception.BusinessException;
import com.itcz.czword.interfaces.service.UserInterfaceInfoService;
import com.itcz.czword.model.common.BaseResponse;
import com.itcz.czword.model.common.ResultUtils;
import com.itcz.czword.model.dto.interfaces.InterfaceAssign;
import com.itcz.czword.model.enums.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户接口控制层")
@RestController
@RequestMapping("/userinterface")
public class UserInterfaceController {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;
    @Operation(summary = "用户分配接口使用次数")
    @PostMapping("/doAssign")
    public BaseResponse<Boolean> doAssign(@RequestBody InterfaceAssign interfaceAssign){
        if(interfaceAssign == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean success = userInterfaceInfoService.doAssign(interfaceAssign);
        return ResultUtils.success(success);
    }
}
