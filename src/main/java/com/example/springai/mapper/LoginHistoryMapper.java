package com.example.springai.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.springai.dto.LoginHistoryDto;
import com.example.springai.dto.UserLoginStat;

@Mapper
public interface LoginHistoryMapper {
    int insertLoginHistory(LoginHistoryDto dto);
    int updateLogoutHistory(LoginHistoryDto dto);
    List<LoginHistoryDto> findById(String userId);
    LoginHistoryDto findBySessionId(String sessionId);
    List<LoginHistoryDto> findAll();
    List<UserLoginStat> getTotalLoginStats();
    List<Map<String, Object>> getUserChatMinutes();
}
