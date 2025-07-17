package com.example.springai.api;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    /** (2) 화면에서 누른 “대화 저장하기” 버튼용 API **/
    @PostMapping("/chat/save")
    public ResponseEntity<Void> saveConversation(@RequestBody List<ChatHistoryDto> history,
                                                 HttpSession session) {
        String sessionId = session.getId();
        for (ChatHistoryDto dto : history) {
            dto.setSessionId(sessionId);
            aiChatService.insertChat(dto);
        }
        return ResponseEntity.ok().build();
    }

    /** (3) 로그인 세션에 담긴 ID를 프런트가 가져갈 수 있는 API **/
    @GetMapping("/whoami")
    public ResponseEntity<Map<String, String>> whoami(HttpSession session) {
        String id = (String) session.getAttribute("id");
        if (id == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(Collections.singletonMap("id", id));
    }
    
    @GetMapping("/chat/history")
    public ResponseEntity<List<ChatHistoryDto>> getHistory(HttpSession session) {
        String sessionId = session.getId();
        List<ChatHistoryDto> history = aiChatService.getChatHistory(sessionId);
        return ResponseEntity.ok(history);
    }
}
    @PostMapping("/chat/bookmark")
    public int updateBookmark(@RequestBody Map<String, Object> body) {
        int no = (int) body.get("no");
        int bookmark = (int) body.get("bookmark");
        return aiChatService.updateBookmark(no, bookmark);
    }

    @PostMapping("/chat/delete")
    public int deleteChats(@RequestBody Map<String, List<Integer>> body) {
        List<Integer> nos = body.get("nos");
        return aiChatService.deleteChats(nos); // 1: 성공, 0: 실패
    }
}
