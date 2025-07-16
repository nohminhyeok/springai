package com.example.springai.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.springai.dto.ChatHistoryDto;

@Mapper
public interface ChatHistoryMapper {

	int insertChat(ChatHistoryDto chatHistoryDto);

}
