package com.itcz.czword.file.controller;

import com.itcz.czword.file.service.FileService;
import com.itcz.czword.model.common.BaseResponse;
import com.itcz.czword.model.common.ResultUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "文件上传接口")
@RestController
@RequestMapping("/file")
public class FileUploadController {
    @Resource
    private FileService fileService;
    @Operation(summary = "文件上传")
    @PostMapping("/upload")
    public BaseResponse<String> upload(@RequestParam(value = "file") MultipartFile multipartFile){
        String fileUrl = fileService.upload(multipartFile);
        return ResultUtils.success(fileUrl);
    }
}
