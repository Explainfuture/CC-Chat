package com.explainsf.springai.service;

import com.explainsf.springai.config.QwenConfig;
import com.explainsf.springai.entity.ChatLog;
import com.explainsf.springai.repository.ChatLogRepository;
import com.explainsf.springai.util.ChatMemoryManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QwenService {

    private final QwenConfig config;
    private final ChatMemoryManager chatMemory;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ChatLogRepository chatLogRepository;

    public String chat(String userId, String message) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // ✅ Step 0：如果 Redis 中没有历史，尝试从数据库恢复
        List<ChatMemoryManager.Message> history = chatMemory.getMessages(userId);
        if (history.isEmpty()) {
            List<ChatLog> recentLogs = chatLogRepository.findTop10ByUserIdOrderByCreateTimeDesc(Long.valueOf(userId));
            // 倒序，保证 oldest → newest
            for (int i = recentLogs.size() - 1; i >= 0; i--) {
                ChatLog log = recentLogs.get(i);
                chatMemory.saveMessage(userId, log.getRole(), log.getContent());
            }
            // 再重新获取一次 Redis 中的上下文
            history = chatMemory.getMessages(userId);
        }

        // ✅ Step 1: 构造 messages 列表
        List<Map<String, String>> messages = new ArrayList<>();
        for (ChatMemoryManager.Message m : history) {
            Map<String, String> entry = new HashMap<>();
            entry.put("role", m.role());
            entry.put("content", m.content());
            messages.add(entry);
        }

        // 添加当前用户输入
        Map<String, String> current = new HashMap<>();
        current.put("role", "user");
        current.put("content", message);
        messages.add(current);

        // ✅ Step 2: 构造请求体
        Map<String, Object> payload = new HashMap<>();
        payload.put("model", "qwen-turbo");
        payload.put("messages", messages);
        String json = objectMapper.writeValueAsString(payload);
        System.out.println("请求 JSON：");
        System.out.println(json);

        // ✅ Step 3: 发请求
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(config.getApiUrl())
                .addHeader("Authorization", "Bearer " + config.getApiKey())
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
            System.out.println("模型返回：");
            System.out.println(responseBody);

            if (responseBody.isEmpty()) {
                return "模型返回为空，请检查请求格式或API Key。";
            }

            JsonNode root = objectMapper.readTree(responseBody);
            String reply = root.path("choices").get(0).path("message").path("content").asText(null);
            if (reply == null) {
                return "模型未返回有效内容，请检查历史记录或token限制。";
            }

            // ✅ Step 4: 写入 Redis 和数据库
            chatMemory.saveMessage(userId, "user", message);
            chatMemory.saveMessage(userId, "assistant", reply);
            saveToDatabase(userId, "user", message);
            saveToDatabase(userId, "assistant", reply);

            return reply;
        }
    }

    private void saveToDatabase(String userIdStr, String role, String content) {
        Long userId = Long.valueOf(userIdStr); // 确保前端传来的 userId 是数据库 user 表的 id
        ChatLog log = new ChatLog();
        log.setUserId(userId);
        log.setRole(role);
        log.setContent(content);
        chatLogRepository.save(log);
    }
}
