package com.explainsf.springai.service.impl;

import com.explainsf.springai.entity.User;
import com.explainsf.springai.model.LoginResponse;
import com.explainsf.springai.repository.UserRepository;
import com.explainsf.springai.service.UserService;
import com.explainsf.springai.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    //BC哈希算法加密
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * 数据库登录方法，每次都要查询，太慢了
     * @param id
     * @param rawPassword
     * @return
     */
    /*@Override
    public User login(Long id, String rawPassword) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                return user;
            }
        }
        return null;
    }*/
    /**
     * jwt令牌登录
     */
    @Override
    public LoginResponse login(Long id, String rawPassword) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                String token = jwtUtil.generateToken(user.getId());
                return new LoginResponse(token, user);
            }
        }
        return null;
    }
}
