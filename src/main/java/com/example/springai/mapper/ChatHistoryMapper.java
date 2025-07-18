package com.example.springai.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.springai.dto.ChatHistoryDto;

@Mapper
public interface ChatHistoryMapper {
    List<ChatHistoryDto> findBySessionId(String sessionId);
    int updateBookmark(Map<String, Object> param); // bookmark 수정
    int deleteChats(List<Integer> nos);
	int insertChat(ChatHistoryDto dto);
	List<ChatHistoryDto> findById(String id);
}
