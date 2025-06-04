package com.explainsf.springai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "qwen")
public class QwenConfig {
    private String apiKey;
    private String apiUrl;
}