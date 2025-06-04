package com.explainsf.springai.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatMemoryManager {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final int MAX_HISTORY = 50;

    public void saveMessage(String userId, String role, String content) {
        String key = "chat:" + userId;
        try {
            List<Message> history = getMessages(userId);
            history.add(new Message(role, content));
            if (history.size() > MAX_HISTORY) {
                history = history.subList(history.size() - MAX_HISTORY, history.size());
            }
            redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(history));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Message> getMessages(String userId) {
        String json = redisTemplate.opsForValue().get("chat:" + userId);
        if (json == null) return new LinkedList<>();
        try {
            return objectMapper.readValue(json, new TypeReference<List<Message>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return new LinkedList<>();
        }
    }

    public static record Message(String role, String content) {}
}