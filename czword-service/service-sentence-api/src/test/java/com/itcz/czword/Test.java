package com.itcz.czword;

import com.itcz.czword.model.entity.randomWord.Sentence;
import com.itcz.czword.sentenceapi.service.SentenceService;
import jakarta.annotation.Resource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@SpringBootTest(classes = {SentenceApiApplication.class})
public class Test {
    @Resource
    private SentenceService sentenceService;
    /**
     * 多线程批量插入数据库
     */
    private ExecutorService executor = new ThreadPoolExecutor(
            60,
            1000,
            10000, TimeUnit.MICROSECONDS,
            new ArrayBlockingQueue<>(1000));
    @org.junit.jupiter.api.Test
    public void getSentence(){
        String url = "http://mingyan123.com/";
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        }catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
        Elements elements = doc.select("mingyan123-ju");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int j = 0;
        List<CompletableFuture> futureList = new ArrayList<>();
        for (Element element : elements) {
            String sentence = element.text();
            for (int i = 0; i < 10; i++) {
                List<Sentence> sentenceList = new ArrayList<>();
                while (true) {
                    j++;
                    Sentence s = new Sentence();
                    s.setRandomWord(sentence);
                    sentenceList.add(s);
                    if(j % 1000 == 0){
                        break;
                    }
                }
                CompletableFuture<Void>future = CompletableFuture.runAsync(()->{
                    sentenceService.saveBatch(sentenceList,10000);
                },executor);
                futureList.add(future);
            }
            CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
        }
        stopWatch.stop();
        if (stopWatch.isRunning()) {
            System.out.println(stopWatch.getLastTaskTimeMillis());
        } else {
            System.out.println("StopWatch is not running.");
        }
    }

    @org.junit.jupiter.api.Test
    public void getSentenceDan() {
        String url = "http://mingyan123.com/";
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        Elements elements = doc.select("mingyan123-ju");
        List<Sentence> sentenceList = new ArrayList<>();
        for (Element element : elements) {
            String sentence = element.text();
            Sentence s = new Sentence();
            s.setRandomWord(sentence);
            sentenceList.add(s);
        }
        boolean batch = sentenceService.saveBatch(sentenceList);
        if(batch){
            System.out.println("插入成功!");
        }else {
            System.out.println("插入失败!");
        }
    }
}
