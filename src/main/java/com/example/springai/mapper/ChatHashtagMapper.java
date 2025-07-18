package com.example.springai.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.springai.dto.ChatHashtagDto;

@Mapper
public interface ChatHashtagMapper {
    List<ChatHashtagDto> findByChatNo(int chatNo);
    int insertHashtag(ChatHashtagDto dto);
    int deleteAllByChatNo(int chatNo); // 기존 태그 전체 삭제 (덮어쓰기용)
    int existsByChatNoAndTag(ChatHashtagDto dto);

}