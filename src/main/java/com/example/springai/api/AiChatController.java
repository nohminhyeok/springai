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

// REST API: AI 챗봇 관련 엔드포인트를 제공하는 컨트롤러
@RestController
public class AiChatController {
    private final AiChatService aiChatService;

    public AiChatController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }
    
    /**
     * AI 챗봇 대화 요청 (OpenAI 서버와 통신)
     * @param chatHistoryDto 사용자 메시지
     * @param session 세션
     * @return AI 응답
     */
    @PostMapping("/chat")
    public String chat(@RequestBody ChatHistoryDto chatHistoryDto, HttpSession session) {
        String userMsg = chatHistoryDto.getUserMsg();
        String aiReply = aiChatService.generate(userMsg, session);
        // [수정] DB 저장 삭제! 프론트에서 저장할 때만 DB에 저장
        return aiReply;
    }

    /**
     * 대화 저장 버튼용 API (여러 대화 이력 저장)
     */
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

    /**
     * 현재 로그인한 사용자 ID 반환 (세션 체크용)
     */
    @GetMapping("/whoami")
    public ResponseEntity<Map<String, String>> whoami(HttpSession session) {
        String id = (String) session.getAttribute("id");
        if (id == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(Collections.singletonMap("id", id));
    }
    
    /**
     * 세션 기준 대화 이력 조회
     */
    @GetMapping("/chat/history")
    public ResponseEntity<List<ChatHistoryDto>> getHistory(HttpSession session) {
        String sessionId = session.getId();
        List<ChatHistoryDto> history = aiChatService.getChatHistory(sessionId);
        return ResponseEntity.ok(history);
    }

    /**
     * 로그인 ID 기준 전체 대화 이력 조회
     */
    @GetMapping("/chat/history/by-id")
    public ResponseEntity<List<ChatHistoryDto>> getHistoryById(HttpSession session) {
        String id = (String) session.getAttribute("id");
        if (id == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<ChatHistoryDto> history = aiChatService.getChatHistoryById(id);
        return ResponseEntity.ok(history);
    }
    
    /**
     * 대화 즐겨찾기(북마크) 수정
     */
    @PostMapping("/chat/bookmark")
    public int updateBookmark(@RequestBody Map<String, Object> body) {
        int no = (int) body.get("no");
        int bookmark = (int) body.get("bookmark");
        return aiChatService.updateBookmark(no, bookmark);
    }

    /**
     * 대화 이력 삭제 (여러 개)
     */
    @PostMapping("/chat/delete")
    public int deleteChats(@RequestBody Map<String, List<Integer>> body) {
        List<Integer> nos = body.get("nos");
        return aiChatService.deleteChats(nos); // 1: 성공, 0: 실패
    }
}
