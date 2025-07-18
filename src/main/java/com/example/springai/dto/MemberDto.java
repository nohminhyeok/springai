package com.example.springai.dto;

import lombok.Data;

@Data
public class MemberDto {
    private String id;
    private String email;
    private String pw;
    private String role;
    // 필요하면 name, phone 등 필드 추가
}
