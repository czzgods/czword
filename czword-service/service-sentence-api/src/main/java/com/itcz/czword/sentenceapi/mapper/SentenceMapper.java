package com.itcz.czword.sentenceapi.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itcz.czword.model.entity.randomWord.Sentence;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 李钟意
* @description 针对表【sentence(毒鸡汤表)】的数据库操作Mapper
* @createDate 2024-03-30 15:52:20
* @Entity generator.domain.Sentence
*/
@Mapper
public interface SentenceMapper extends BaseMapper<Sentence> {

    List<String> selectContextList();

}




