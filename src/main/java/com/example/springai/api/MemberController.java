package com.example.springai.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.springai.dto.MemberDto;
import com.example.springai.service.MemberService;
import com.example.springai.service.LoginHistoryService;
import jakarta.servlet.http.HttpSession;

@RestController
public class MemberController {
    private final MemberService memberService;
    private final LoginHistoryService loginHistoryService;
    
    public MemberController(MemberService memberService, LoginHistoryService loginHistoryService) {
        this.memberService = memberService;
        this.loginHistoryService = loginHistoryService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody MemberDto dto, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            MemberDto member = memberService.login(dto.getId(), dto.getPw());
            if (member != null) {
                // 로그인 성공 - 세션에 사용자 정보 저장
                session.setAttribute("id", member.getId());
                session.setAttribute("loginId", member.getId());
                
                // 로그인 기록 저장
                loginHistoryService.recordLogin(member.getId(), session.getId());
                
                response.put("success", true);
                response.put("message", "로그인 성공");
                response.put("member", member);
                
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "아이디 또는 비밀번호가 올바르지 않습니다.");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "로그인 처리 중 오류가 발생했습니다.");
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/join")
    public ResponseEntity<Map<String, Object>> join(@RequestBody MemberDto dto) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 아이디 중복 체크
            if (memberService.isIdDuplicate(dto.getId())) {
                response.put("success", false);
                response.put("message", "이미 사용 중인 아이디입니다.");
                return ResponseEntity.ok(response);
            }
            
            // 회원가입 처리
            int result = memberService.join(dto);
            if (result > 0) {
                response.put("success", true);
                response.put("message", "회원가입이 완료되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "회원가입에 실패했습니다.");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "회원가입 처리 중 오류가 발생했습니다.");
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        String id = (String) session.getAttribute("id");
        String sessionId = session.getId();
        
        session.invalidate();
        
        // 로그아웃 기록 저장
        if (id != null) {
            loginHistoryService.recordLogout(id, sessionId);
        }
        
        response.put("success", true);
        response.put("message", "로그아웃되었습니다.");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/member/info")
    public MemberDto getMemberInfo(HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");
        return memberService.getMemberinfo(loginId);
    }

    @PostMapping("/user/modify")
    public int modifyUser(@RequestBody MemberDto dto) {
        return memberService.updateMember(dto);
    }
    
    @GetMapping("/loginHistory")
    public ResponseEntity<?> loginHistory(HttpSession session) {
        String id = (String) session.getAttribute("id");
        if (id == null) {
            return ResponseEntity.status(401).body(null);
        }
        return ResponseEntity.ok(loginHistoryService.getUserLoginHistory(id));
    }
}
