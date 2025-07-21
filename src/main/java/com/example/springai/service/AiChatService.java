package com.example.springai.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

import com.example.springai.dto.ChatHistoryDto;
import com.example.springai.mapper.ChatHistoryMapper;

import jakarta.servlet.http.HttpSession;

@Service
public class AiChatService {
	// OpenAI 챗봇 모델, DB 매퍼 의존성 주입
	private OpenAiChatModel openAiChatModel;
	private ChatHistoryMapper chatHistoryMapper; 
	/**
	 * 생성자: OpenAI 챗봇 모델, DB 매퍼 주입
	 * @param openAiChatModel OpenAI 챗봇 모델
	 * @param chatHistoryMapper 챗 이력 DB 매퍼
	 */
	public AiChatService(OpenAiChatModel openAiChatModel, ChatHistoryMapper chatHistoryMapper) {
		this.openAiChatModel = openAiChatModel;
		this.chatHistoryMapper = chatHistoryMapper;
	}
	
	// OpenAI 서버와 통신할 메서드 선언
	// param String userMsg : 사용자 문자열
	// return String : 챗 서버의 응답문자열
	public String generate(String userMsg, HttpSession session) {
	      /*
	       * DB 저장될 내용
	       * userMsg
	       * sessiong.getId()
	       * 
	       * ex)
	       * 1(int), 12312...(text), 안녕(text), 그래 ...(text)
	       * 
	       */
		List<Message> messageList = (List<Message>)session.getAttribute("chatHistory");
		if(messageList == null) {
			messageList = new ArrayList<Message>(); // 이전 이력없이 처음 대화이다.
		}
		
		// SystemMessage, UserMessage
		SystemMessage systemMessage = new SystemMessage("너는 한국어로 대답하고 반말로만 답변하는 테토녀 스타일이야");
		UserMessage userMessage = new UserMessage(userMsg); 
		messageList.add(systemMessage);
		messageList.add(userMessage);

		
		OpenAiChatOptions options = OpenAiChatOptions.builder()
				.model("gpt-3.5-turbo") // 사용하고자 하는 OpenAI 모델(버전)의 이름을 지정
				.temperature(0.7) // 창의성(무작위성) 정도를 설정(0.0 ~ 2.0)값으로 보통 0~1 사이 사용
				.build();
		
		
		// 프롬포트 : openAI서버에 전달되는 모델 객체
	    Prompt prompt = new Prompt(messageList, options);

	    
	    // openAiChatModel빈을 통해 prompt를 OpenAI 서버에 전달
	    ChatResponse res = this.openAiChatModel.call(prompt);
	    String aiReply = res.getResult().getOutput().getText();
	    // AI가 응답한 내용들도 messageList에 누적
	    AssistantMessage assistantMessage = new AssistantMessage(aiReply);
	    messageList.add(assistantMessage);
	    
	    // messageList 변경된 내용 session의 messageList 속성에도 반영
	    session.setAttribute("chatHistory", messageList); // 이전 session chatHistory 속성값을 덮어쓰기
	    
	    // multi-turn 기능이 없어 기억력이 0
	    
	    // DAO 호출
	    
	    return aiReply;
	}
    // 채팅 이력 저장
    public int insertChat(ChatHistoryDto dto) {
        return chatHistoryMapper.insertChat(dto);
    }

    // 세션ID로 대화 이력 조회
    public List<ChatHistoryDto> getChatHistory(String sessionId) {
        return chatHistoryMapper.findBySessionId(sessionId);
    }

    // 유저ID로 대화 이력 조회
    public List<ChatHistoryDto> getChatHistoryById(String id) {
        return chatHistoryMapper.findById(id);
    }

    // 북마크 변경
    public int updateBookmark(int no, int bookmark) {
        Map<String, Object> param = new HashMap<>();
        param.put("no", no);
        param.put("bookmark", bookmark);
        return chatHistoryMapper.updateBookmark(param);
    }

    // 다중 삭제
    public int deleteChats(List<Integer> nos) {
        if(nos == null || nos.isEmpty()) return 0;
        return chatHistoryMapper.deleteChats(nos) > 0 ? 1 : 0;
    }
}
