package com.example.springai.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.springai.dto.LoginHistoryDto;
import com.example.springai.mapper.LoginHistoryMapper;

@Service
public class LoginHistoryService {
    // 로그인 이력 매퍼 DI
    private final LoginHistoryMapper mapper;
    
    // 생성자
    public LoginHistoryService(LoginHistoryMapper mapper) {
        this.mapper = mapper;
    }

    // 로그인 기록 저장
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
    
    // 로그아웃 기록 + 머문 시간 계산/저장
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

    // 본인 로그인 이력 리스트
    public List<LoginHistoryDto> getUserLoginHistory(String id) {
        return mapper.findById(id);
    }

    // 전체 로그인 이력 리스트
    public List<LoginHistoryDto> getAllLoginHistory() {
        return mapper.findAll();
    }

    // 전체 유저별 총 접속시간 통계
    public List<com.example.springai.dto.UserLoginStat> getTotalLoginStats() {
        return mapper.getTotalLoginStats();
    }

    // 최근 N일간 일자별 총 대화수 (임시 샘플)
    public List<Map<String, Object>> getDailyChatCounts(int days) {
        List<Map<String, Object>> list = new java.util.ArrayList<>();
        java.time.LocalDate today = java.time.LocalDate.now();
        for (int i = days-1; i >= 0; i--) {
            java.time.LocalDate d = today.minusDays(i);
            Map<String, Object> row = new java.util.HashMap<>();
            row.put("date", d.toString());
            row.put("count", 100 + (int)(Math.random()*100)); // 샘플 랜덤값
            list.add(row);
        }
        return list;
    }

    // 사용자별 챗봇 사용시간 (DB)
    public List<Map<String, Object>> getUserChatMinutes() {
        return mapper.getUserChatMinutes();
    }
}
