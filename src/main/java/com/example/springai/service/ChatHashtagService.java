package com.example.springai.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.springai.mapper.ChatHashtagMapper;
import com.example.springai.dto.ChatHashtagDto;

@Service
public class ChatHashtagService {
	// 해시태그 매퍼 DI
	private final ChatHashtagMapper chatHashtagMapper;

	// 생성자
	public ChatHashtagService(ChatHashtagMapper chatHashtagMapper) {
		this.chatHashtagMapper = chatHashtagMapper;
	}

	// 특정 채팅의 태그 리스트 반환
	public List<String> getTags(int chatNo) {
	    List<ChatHashtagDto> list = chatHashtagMapper.findByChatNo(chatNo);
	    if (list == null) list = List.of();
	    return list.stream().map(ChatHashtagDto::getTagText).collect(Collectors.toList());
	}

	// 태그 중복 없이 저장
	public void setTags(int chatNo, List<String> tags) {
		tags.stream().distinct().forEach(tagText -> {
			ChatHashtagDto dto = new ChatHashtagDto();
			dto.setChatNo(chatNo);
			dto.setTagText(tagText);
			// 중복 체크 후 insert
			if (chatHashtagMapper.existsByChatNoAndTag(dto) == 0) {
				chatHashtagMapper.insertHashtag(dto);
			}
		});
	}

	// 인기 해시태그 리스트 반환
	public List<Map<String, Object>> selectPopularTags() {
		return chatHashtagMapper.selectPopularTags();
	}

	// 전체 해시태그 리스트 반환
	public List<Map<String, Object>> selectAllTags() {
		return chatHashtagMapper.selectAllTags();
	}

	// 특정 태그가 포함된 채팅 리스트 반환
	public List<Map<String, Object>> getChatsByTag(String tagText) {
		return chatHashtagMapper.selectChatsByTag(tagText);
	}

	// 전체 채팅 + 태그 리스트 반환
	public List<Map<String, Object>> getAllChatsWithTags() {
		return chatHashtagMapper.selectAllChatsWithTags();
	}
}
