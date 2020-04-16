package com.aaa.springbootwebmagic.service.impl;
import com.aaa.springbootwebmagic.domain.entity.User;
import com.aaa.springbootwebmagic.mapper.UserMapper;
import com.aaa.springbootwebmagic.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2019/12/17
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
