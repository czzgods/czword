package com.itcz.czword.sentenceapi.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.itcz.czword.model.entity.randomWord.Sentence;

/**
* @author 李钟意
* @description 针对表【sentence(毒鸡汤表)】的数据库操作Service
* @createDate 2024-03-30 15:52:20
*/
public interface SentenceService extends IService<Sentence> {

    String getRandomSentence();
}
