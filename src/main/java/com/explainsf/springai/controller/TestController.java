package com.explainsf.springai.controller;

import com.explainsf.springai.entity.User;
import com.explainsf.springai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/redis")
    public String testRedis(){
        //写入redis
        stringRedisTemplate.opsForValue().set("test - key", "hello redis!");
        //从redis里读
        String value = stringRedisTemplate.opsForValue().get("test - key");
        return "redis 中的值是：" + value;
    }

    @GetMapping("/db")
    public String testDb(){
        //创建用户对象
        User user = new User();
        user.setName("Alice");
        user.setEmail("alice@example.com");

        //保存到数据库里
        userRepository.save(user);

        //查询用户总数
        long count = userRepository.count();
        return "用户总数为：" + count;
    }

}
