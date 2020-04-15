package com.aaa.springbootwebmagic.web;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/15
 */


import com.aaa.springbootwebmagic.config.redis.BloomFilterHelper;
import com.aaa.springbootwebmagic.config.redis.RedisBloomFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class RedisBloomFilterController {

    @Autowired
    private BloomFilterHelper bloomFilterHelper;
    @Resource
    private RedisBloomFilterService redisService;

    @RequestMapping("/bloom/addByBloomFilter")
    public void add(){
        redisService.addByBloomFilter(bloomFilterHelper, "bloom", "testKey1");

    }

    @RequestMapping("/bloom/includeByBloomFilter")
    public boolean exists(){
        return redisService.includeByBloomFilter(bloomFilterHelper, "bloom", "testKey1");
    }

}
