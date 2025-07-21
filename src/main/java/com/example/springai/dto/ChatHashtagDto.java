package com.example.springai.dto;

import lombok.Data;

@Data
public class ChatHashtagDto {
    private int chatNo;
    private String tagText;
    private java.sql.Timestamp createAt;
}