package com.example.springai.dto;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class LoginHistoryDto {
    private int idkey;
    private String id;
    private Timestamp loginTime;
    private Timestamp logoutTime;
    private Integer durationMinutes;
    private String sessionId;
    private Timestamp createdAt;
}
