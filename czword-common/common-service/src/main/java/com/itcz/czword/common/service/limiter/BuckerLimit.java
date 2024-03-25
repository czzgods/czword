package com.itcz.czword.common.service.limiter;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class BuckerLimit {
    @Resource
    private RedisTemplate redisTemplate;
    //桶最大容量
    private Long maxBucket;

    //放入令牌的速度
    private Long inputSpeed;

    //目前剩余的令牌数量
    private Long restToken;

    //桶初始化的时间
    private Long initTime;

    //令牌的名称(限流的对象为ip地址)
    private String host;

    public BuckerLimit(){}

    public BuckerLimit(Long maxBucket, Long inputSpeed){
        this.maxBucket=maxBucket;
        this.inputSpeed=inputSpeed;
    }

    public void setMaxBucket(Long maxBucket) {
        this.maxBucket = maxBucket;
    }

    public void setInputSpeed(Long inputSpeed) {
        this.inputSpeed = inputSpeed;
    }

    private static final String LAST_REQUEST_TIME="limit:lasttime";

    private static final String LIMIT_HOST="limit:host:";

    public synchronized boolean acquire(String host,Long initTime){
        this.initTime=initTime;//当前时间
        this.host=host;//请求IP地址
        //首先获取上一次请求后的令牌桶状态
        BucketStatus lastRequestBucketStatus = getLastRequestBucketStatus();
        //获取上一次请求的时间
        Long lastRequestTime = lastRequestBucketStatus.getRequestTime();

        //获取上一次请求的剩余令牌数量
        Long lastResidueKey = lastRequestBucketStatus.getResidueKey();

        //获取现在请求的时间
        long nowRequestTime = System.currentTimeMillis();
        //获取上一次请求到现在这一次请求所需要生成的令牌数量
        restToken = Math.min(maxBucket, (((nowRequestTime - lastRequestTime) / 1000) * inputSpeed)+ lastResidueKey);
        if( restToken > 0){
            System.out.println("令牌剩余数量:"+restToken+new Date());
            //如果令牌数大于0
            //消耗一个令牌
            restToken--;
            //存入redis中
            //首先删除
            redisTemplate.delete(LIMIT_HOST+this.host);
            //再将新的令牌数量添加进去
            redisTemplate.opsForValue().set(LIMIT_HOST+this.host,restToken);
            redisTemplate.delete(LAST_REQUEST_TIME+":"+this.host);
            redisTemplate.opsForValue().set(LAST_REQUEST_TIME+":"+this.host,nowRequestTime);
            return true;
        }
        //若令牌数量 小于1 则 不允许通过
        return false;
    }


    /**
     * 获取上一次请求后的令牌状态
     * @return
     */
    private BucketStatus getLastRequestBucketStatus(){
        //获取上次请求后剩余的令牌数量
        Object o = redisTemplate.opsForValue().get(LIMIT_HOST+this.host);
        Long tokenNum=null;
        if(o !=null){
            tokenNum=Long.valueOf(o.toString());
        }
        Long lastReuqestTime = (Long) redisTemplate.opsForValue().get(LAST_REQUEST_TIME+":"+this.host);
        if(lastReuqestTime == null){
            //说明是第一次请求
            return new BucketStatus(2L,this.initTime);
        }
        return new BucketStatus(tokenNum,lastReuqestTime);
    }

    /**
     * 令牌桶的状态
     */
    public static class BucketStatus {
        /* 剩余令牌数 */
        private final Long residueKey;
        /* 请求时间 */
        private final Long requestTime;

        public BucketStatus(Long residueKey, Long requestTime) {
            this.residueKey = residueKey;
            this.requestTime = requestTime;
        }

        public Long getResidueKey() {
            return residueKey;
        }

        public Long getRequestTime() {
            return requestTime;
        }
    }
}
