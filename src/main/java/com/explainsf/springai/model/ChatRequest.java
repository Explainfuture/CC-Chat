package com.explainsf.springai.model;

import lombok.Data;

@Data
public class ChatRequest {
    private String userId;
    private String message;
}
