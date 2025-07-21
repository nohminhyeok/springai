package com.example.springai.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import com.example.springai.dto.LoginHistoryDto;
import com.example.springai.mapper.LoginHistoryMapper;

@Service
public class LoginHistoryService {
    private final LoginHistoryMapper mapper;
    
    public LoginHistoryService(LoginHistoryMapper mapper) {
        this.mapper = mapper;
    }

    public void recordLogin(String id, String sessionId) {
        System.out.println("recordLogin 호출: id=" + id + ", sessionId=" + sessionId);
        LoginHistoryDto h = new LoginHistoryDto();
        h.setId(id);
        h.setSessionId(sessionId);
        h.setLoginTime(java.sql.Timestamp.valueOf(LocalDateTime.now()));
        h.setCreatedAt(java.sql.Timestamp.valueOf(LocalDateTime.now()));
        System.out.println("삽입 DTO=" + h);
        int result = mapper.insertLoginHistory(h);
        System.out.println("삽입 결과=" + result);
    }
    
    public void recordLogout(String id, String sessionId) {
        System.out.println("로그아웃 호출: id=" + id + ", sessionId=" + sessionId);
        LoginHistoryDto h = mapper.findBySessionId(sessionId);
        System.out.println("조회된 이력: " + h);
        if (h != null) {
            LocalDateTime loginTime = h.getLoginTime().toLocalDateTime();
            LocalDateTime out = LocalDateTime.now();
            h.setLogoutTime(java.sql.Timestamp.valueOf(out));
            h.setDurationMinutes((int) ChronoUnit.MINUTES.between(loginTime, out));
            int result = mapper.updateLogoutHistory(h);
            System.out.println("업데이트 결과=" + result + ", 업데이트 DTO=" + h);
        } else {
            System.out.println("해당 세션으로 로그인 이력 없음");
        }
    }


    public List<LoginHistoryDto> getUserLoginHistory(String id) {
        return mapper.findById(id);
    }

    public List<LoginHistoryDto> getAllLoginHistory() {
        return mapper.findAll();
    }

    public List<com.example.springai.dto.UserLoginStat> getTotalLoginStats() {
        return mapper.getTotalLoginStats();
    }
}
