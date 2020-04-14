package com.aaa.springbootwebmagic.web;

import com.aaa.springbootwebmagic.domain.User;
import com.aaa.springbootwebmagic.service.UserMapper;
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

    @RequestMapping
    public List<User> getUserListAll(){

        return userMapper.selectList(null);
    }
}
