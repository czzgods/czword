package com.itcz.czword.sentenceapi.controller;

import com.itcz.czword.model.common.BaseResponse;
import com.itcz.czword.model.common.ResultUtils;
import com.itcz.czword.sentenceapi.service.SentenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "随机一言接口")
@RestController
@RequestMapping("/api/auth")
public class SentenceController {
    @Resource
    private SentenceService sentenceService;
    @Operation(summary = "获取随机毒鸡汤")
    @GetMapping("/randomWord")
    public BaseResponse<String> getRandomSentence(){
        String sentence = sentenceService.getRandomSentence();
        return ResultUtils.success(sentence);
    }
}
