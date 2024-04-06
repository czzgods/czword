package com.itcz.czword.interfaces.controller;

import com.itcz.czword.common.service.annotation.AuthCheck;
import com.itcz.czword.common.service.exception.BusinessException;
import com.itcz.czword.interfaces.service.InterfaceService;
import com.itcz.czword.interfaces.service.UserInterfaceInfoService;
import com.itcz.czword.model.common.BaseResponse;
import com.itcz.czword.model.common.ResultUtils;
import com.itcz.czword.model.constant.UserConstant;
import com.itcz.czword.model.entity.interfaces.Interface;
import com.itcz.czword.model.entity.interfaces.UserInterfaceInfo;
import com.itcz.czword.model.enums.ErrorCode;
import com.itcz.czword.model.vo.interfaces.InterfaceVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/analysis")
@Slf4j
@Tag(name = "接口统计分析")
public class AnalysisController {
    @Resource
    private InterfaceService interfaceService;

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Operation(summary = "接口统计分析")
    @GetMapping("/top/interface/invoke")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<InterfaceVo>> listTopInvokeInterfaceInfo(){
        //查询调用次数前三的接口
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoService.listTopInvokeInterfaceInfo(3);
        if(userInterfaceInfoList.isEmpty()){
            throw new BusinessException(ErrorCode.INTERFACE_NOT_EXIST);
        }
        //根据接口id进行分组
        Map<Long, List<UserInterfaceInfo>> interfaceInfoIdObjMap = userInterfaceInfoList.stream()
                .collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        //查询接口信息
        List<Interface> list = interfaceService.lambdaQuery()
                .in(Interface::getId, interfaceInfoIdObjMap.keySet())
                .list();
        if(list.isEmpty()){
            throw new BusinessException(ErrorCode.INTERFACE_NOT_EXIST);
        }
        //封装接口信息并返回
        List<InterfaceVo> result = list.stream().map(anInterface -> {
            InterfaceVo interfaceVo = new InterfaceVo();
            BeanUtils.copyProperties(anInterface, interfaceVo);
            //调用次数
            Integer totalNum = interfaceInfoIdObjMap.get(anInterface.getId()).get(0).getTotalNum();
            interfaceVo.setTotalNum(totalNum);
            return interfaceVo;
        }).collect(Collectors.toList());
        return ResultUtils.success(result);
    }

    @Operation(summary = "统计接口调用总次数")
    @GetMapping("/top/interface/allCount")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<InterfaceVo> allCount(){
        InterfaceVo interfaceVo = userInterfaceInfoService.allCount();
        return ResultUtils.success(interfaceVo);
    }
}
