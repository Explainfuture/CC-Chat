package com.explainsf.springai.controller;

import com.explainsf.springai.entity.User;
import com.explainsf.springai.model.LoginResponse;
import com.explainsf.springai.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 用户注册接口
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        User saved = userService.register(user);
        return ResponseEntity.ok(saved);
    }

    // 用户登录接口
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam Long id, @RequestParam String password) {
        //User user = userService.login(id, password);
        LoginResponse response = userService.login(id, password);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body("用户名或密码错误");
        }
    }
}
