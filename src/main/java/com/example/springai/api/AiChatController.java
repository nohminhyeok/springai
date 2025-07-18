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
    public String chat(@RequestBody ChatHistoryDto chatHistoryDto, HttpSession session) {
        String userMsg = chatHistoryDto.getUserMsg();
        String aiReply = aiChatService.generate(userMsg, session);
        // [수정] DB 저장 삭제! 프론트에서 저장할 때만 DB에 저장
        return aiReply;
    }

    // 대화 저장 버튼용 API
    @PostMapping("/chat/save")
    public ResponseEntity<Void> saveConversation(
        @RequestBody List<ChatHistoryDto> history,
        HttpSession session
    ) {
        String sessionId = session.getId();
        String id = (String) session.getAttribute("id");
        for (ChatHistoryDto dto : history) {
            dto.setSessionId(sessionId);
            if (id != null) {
                dto.setId(id);
                dto.setBookmark(dto.getBookmark() == 0 ? 0 : dto.getBookmark()); // [수정] bookmark값 기본값 처리
                aiChatService.insertChat(dto);
            }
        }
        return ResponseEntity.ok().build();
    }

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

    @GetMapping("/chat/history/by-id")
    public ResponseEntity<List<ChatHistoryDto>> getHistoryById(HttpSession session) {
        String id = (String) session.getAttribute("id");
        if (id == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<ChatHistoryDto> history = aiChatService.getChatHistoryById(id);
        return ResponseEntity.ok(history);
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
