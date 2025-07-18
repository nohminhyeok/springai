package com.example.springai.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.example.springai.dto.LoginHistoryDto;

@Mapper
public interface LoginHistoryMapper {
    int insertLoginHistory(LoginHistoryDto dto);
    int updateLogoutHistory(LoginHistoryDto dto);
    List<LoginHistoryDto> findById(String userId);
    LoginHistoryDto findBySessionId(String sessionId);
}
