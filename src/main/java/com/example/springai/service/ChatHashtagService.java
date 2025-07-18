package com.example.springai.service;

//com.example.springai.service.ChatHashtagService.java
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import com.example.springai.mapper.ChatHashtagMapper;
import com.example.springai.dto.ChatHashtagDto;

@Service
public class ChatHashtagService {
 private final ChatHashtagMapper mapper;

 public ChatHashtagService(ChatHashtagMapper mapper) {
     this.mapper = mapper;
 }

 public List<String> getTags(int chatNo) {
     return mapper.findByChatNo(chatNo)
             .stream().map(ChatHashtagDto::getTag).collect(Collectors.toList());
 }

 public void setTags(int chatNo, List<String> tags) {
	    tags.stream()
	        .distinct()
	        .forEach(tag -> {
	            ChatHashtagDto dto = new ChatHashtagDto();
	            dto.setChatNo(chatNo);
	            dto.setTag(tag);
	            // 이미 존재하면 insert하지 않음
	            if (mapper.existsByChatNoAndTag(dto) == 0) {
	                mapper.insertHashtag(dto);
	            }
	        });
	}

}
