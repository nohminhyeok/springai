package com.example.springai.listener;

import com.example.springai.service.LoginHistoryService;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.stereotype.Component;

@Component
public class SessionListener implements HttpSessionListener {
    private final LoginHistoryService loginHistoryService;

    public SessionListener(LoginHistoryService loginHistoryService) {
        this.loginHistoryService = loginHistoryService;
    }
    
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // 로그인 처리 시 세션에 id 를 setAttribute("id", userId) 해 주셔야 합니다.
        System.out.println("Session created: " + se.getSession().getId());
    }
    
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        String id        = (String) se.getSession().getAttribute("id");
        String sessionId = se.getSession().getId();
        if (id != null) {
            loginHistoryService.recordLogout(id, sessionId);
        }
    }
}
