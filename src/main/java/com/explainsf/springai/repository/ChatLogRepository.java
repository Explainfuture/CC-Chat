package com.explainsf.springai.repository;

import com.explainsf.springai.entity.ChatLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {
    List<ChatLog> findByUserIdOrderByCreateTimeAsc(Long userId);

    List<ChatLog> findTop10ByUserIdOrderByCreateTimeDesc(Long userId);
}
