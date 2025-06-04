package com.explainsf.springai.controller;

import com.explainsf.springai.model.ChatRequest;
import com.explainsf.springai.model.Result;
import com.explainsf.springai.service.QwenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class ChatController {
    private final QwenService qwenService;

    /**
     * 更新的写法，前端页面调试
     * @return
     */
    @GetMapping("/")
    public String chatPage() {
        return "chat"; // chat.html 必须在 resources/templates 下
    }
    @PostMapping("/chat")
    @ResponseBody
    public Result<String> chat(@RequestBody ChatRequest request) {
        try {
            String reply = qwenService.chat(request.getUserId(), request.getMessage());
            return Result.success(reply);
        } catch (Exception e) {
            return (Result<String>) Result.error("AI 响应失败：" + e.getMessage());
        }
    }
    /**
     * 这是原始的写法，通过get 的url访问api
     */
    /*
    @GetMapping
    public String ask(@RequestParam String message) throws IOException {
        return qwenService.chat(message);
    }
    */

    /**
     * 新写法，使用post请求
     */
    /* 由于变为前端页面调试，postman测试接口也可以隐藏了
    @PostMapping
    public Result<String> chat(@RequestBody ChatRequest chatRequest) throws IOException {
        String reply = qwenService.chat(chatRequest.getMessage());
        return Result.success(reply);
    }
    */
}
