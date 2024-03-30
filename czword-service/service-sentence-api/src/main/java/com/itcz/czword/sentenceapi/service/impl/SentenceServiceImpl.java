package com.itcz.czword.sentenceapi.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.itcz.czword.model.entity.randomWord.Sentence;
import com.itcz.czword.sentenceapi.mapper.SentenceMapper;
import com.itcz.czword.sentenceapi.service.SentenceService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 李钟意
* @description 针对表【sentence(毒鸡汤表)】的数据库操作Service实现
* @createDate 2024-03-30 15:52:20
*/
@Service
public class SentenceServiceImpl extends ServiceImpl<SentenceMapper, Sentence>
    implements SentenceService {
    @Resource
    private SentenceMapper sentenceMapper;
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    private static final String RANDOM_WORD="random:word";
    @Override
    public String getRandomSentence() {
        //判断redis中是否存在数据
        Boolean hasKey = redisTemplate.hasKey(RANDOM_WORD);
        if(hasKey) {
            Long size = redisTemplate.opsForList().size(RANDOM_WORD);
            //在0~size中生成随机数
            int randomInt = RandomUtil.randomInt(0, size.intValue());
            //从redis中查询
            String sentence = redisTemplate.opsForList().index(RANDOM_WORD, randomInt);
            return sentence;
        }
        //到这里说明redis中没有数据
        List<String> sentenceList = sentenceMapper.selectContextList();
        int size = sentenceList.size();
        //将数据添加到redis中
        redisTemplate.opsForList().rightPushAll(RANDOM_WORD,sentenceList);
        return sentenceList.get(RandomUtil.randomInt(0,size));
    }
}




