package com.example.springai.dto;

import lombok.Data;

@Data
public class ChatHistoryDto {
	private int key;
	private String sessionId;
	private String userMsg;
	private String aiReply;
}
