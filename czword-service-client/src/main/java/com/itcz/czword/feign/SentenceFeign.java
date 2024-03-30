package com.itcz.czword.feign;

import com.itcz.czword.model.common.BaseResponse;
import com.itcz.czword.model.dto.interfaces.InterfaceRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "service-sentence-api")
public interface SentenceFeign {
    @GetMapping ("/api/sentence")
    String getRandomSentence();
}
