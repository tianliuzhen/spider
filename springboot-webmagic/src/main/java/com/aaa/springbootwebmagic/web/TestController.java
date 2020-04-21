package com.aaa.springbootwebmagic.web;

import com.aaa.springbootwebmagic.domain.entity.User;
import com.aaa.springbootwebmagic.job.ChineseZodiacV1Processor;
import com.aaa.springbootwebmagic.job.ChineseZodiacV2Processor;
import com.aaa.springbootwebmagic.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/14
 */
@RestController
public class TestController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    ChineseZodiacV2Processor chineseZodiacV2Processor;
    @Autowired
    ChineseZodiacV1Processor chineseZodiacV1Processor;

    @RequestMapping(value = "/chineseZodiacV2Processor")
    public void chineseZodiacV2Processor(){

         chineseZodiacV2Processor.main();
    }
    @RequestMapping(value = "/chineseZodiacV1Processor")
    public void chineseZodiacV1Processor(){

        chineseZodiacV1Processor.main();
    }
    @RequestMapping(value = "/getUserListAll")
    public List<User> getUserListAll(){

        return userMapper.selectList(null);
    }
}
