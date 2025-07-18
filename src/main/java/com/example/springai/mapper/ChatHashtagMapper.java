package com.example.springai.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.springai.dto.ChatHashtagDto;

@Mapper
public interface ChatHashtagMapper {
    List<ChatHashtagDto> findByChatNo(int chatNo);
    int insertHashtag(ChatHashtagDto dto);
    int deleteAllByChatNo(int chatNo); // 기존 태그 전체 삭제 (덮어쓰기용)
    int existsByChatNoAndTag(ChatHashtagDto dto);
	List<Map<String, Object>> selectPopularTags();
	List<Map<String, Object>> selectAllTags();
	List<Map<String, Object>> selectChatsByTag(String tagText);
	List<Map<String, Object>> selectAllChatsWithTags();

}