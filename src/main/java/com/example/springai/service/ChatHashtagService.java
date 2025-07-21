package com.example.springai.service;

//com.example.springai.service.ChatHashtagService.java
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.springai.mapper.ChatHashtagMapper;
import com.example.springai.dto.ChatHashtagDto;

@Service
public class ChatHashtagService {
	private final ChatHashtagMapper chatHashtagMapper;

	public ChatHashtagService(ChatHashtagMapper chatHashtagMapper) {
		this.chatHashtagMapper = chatHashtagMapper;
	}

	public List<String> getTags(int chatNo) {
	    List<ChatHashtagDto> list = chatHashtagMapper.findByChatNo(chatNo);
	    if (list == null) list = List.of();
	    return list.stream().map(ChatHashtagDto::getTagText).collect(Collectors.toList());
	}


	public void setTags(int chatNo, List<String> tags) {
		tags.stream().distinct().forEach(tagText -> {
			ChatHashtagDto dto = new ChatHashtagDto();
			dto.setChatNo(chatNo);
			dto.setTagText(tagText);
			// 이미 존재하면 insert하지 않음
			if (chatHashtagMapper.existsByChatNoAndTag(dto) == 0) {
				chatHashtagMapper.insertHashtag(dto);
			}
		});
	}

	public List<Map<String, Object>> selectPopularTags() {
		return chatHashtagMapper.selectPopularTags();
	}

	public List<Map<String, Object>> selectAllTags() {
		return chatHashtagMapper.selectAllTags();
	}

	public List<Map<String, Object>> getChatsByTag(String tagText) {
		return chatHashtagMapper.selectChatsByTag(tagText);
	}

	public List<Map<String, Object>> getAllChatsWithTags() {
		return chatHashtagMapper.selectAllChatsWithTags();
	}

}
