package com.example.springai.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ChatHistoryDto {
    private int chatNo;
    private String id;
    private String sessionId;
    private String userMsg;
    private String aiReply;
    private Timestamp createAt;
    private int bookmark; // 즐겨찾기 컬럼 추가
}
