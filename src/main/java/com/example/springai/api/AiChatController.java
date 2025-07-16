package com.example.springai.api;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.springai.dto.ChatHistoryDto;
import com.example.springai.service.AiChatService;

import jakarta.servlet.http.HttpSession;

@RestController
public class AiChatController {
	private final AiChatService aiChatService;

	public AiChatController(AiChatService aiChatService) {
		this.aiChatService = aiChatService;
	}
	
    @PostMapping("/chat")
    public String chat(@RequestBody ChatHistoryDto chatHistoryDto, HttpSession session) { // session 속성 안에 message List를 만들어 이전 대화를 누적
    	// {"userMsg":"hello"} JSON 문자열 -> 자바 DTO 객체(@RequestBody)
    	String userMsg = chatHistoryDto.getUserMsg();
    	String aiReply = aiChatService.generate(userMsg, session);
    	chatHistoryDto.setSessionId(session.getId());
    	chatHistoryDto.setAiReply(aiReply);
    	int row = aiChatService.insertChat(chatHistoryDto);

    	return aiReply;
    
    }
}
