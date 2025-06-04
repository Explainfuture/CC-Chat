package com.explainsf.springai.service;

import com.explainsf.springai.entity.User;
import com.explainsf.springai.model.LoginResponse;

public interface UserService {
    /**
     * 从数据库里查询的登录
     */
    /*
    User register(User user);
    User login(Long id, String password);
    */
    /**
     * JWT生成令牌的登录
     */
    User register(User user);
    LoginResponse login(Long id, String password);
}
