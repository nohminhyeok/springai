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

// 회원/로그인 관련 REST API 컨트롤러
@RestController
public class MemberController {
    private final MemberService memberService;
    private final LoginHistoryService loginHistoryService;
    
    public MemberController(MemberService memberService, LoginHistoryService loginHistoryService) {
        this.memberService = memberService;
        this.loginHistoryService = loginHistoryService;
    }

    /**
     * 로그인 처리 (세션/role 저장)
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody MemberDto dto, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            MemberDto member = memberService.login(dto.getId(), dto.getPw());
            if (member != null) {
                // 로그인 성공 - 세션에 사용자 정보 저장
                session.setAttribute("id", member.getId());
                session.setAttribute("loginId", member.getId());
                session.setAttribute("role", member.getRole());
                System.out.println("[로그인] 세션에 저장된 role: " + member.getRole());
                // 로그인 기록 저장
                loginHistoryService.recordLogin(member.getId(), session.getId());
                response.put("success", true);
                response.put("message", "로그인 성공");
                response.put("member", member);
                response.put("role", member.getRole());
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

    /**
     * 회원가입 처리
     */
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

    /**
     * 로그아웃 처리 (세션 만료)
     */
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

    /**
     * 현재 로그인한 사용자 정보 반환
     */
    @GetMapping("/member/info")
    public MemberDto getMemberInfo(HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");
        return memberService.getMemberinfo(loginId);
    }

    /**
     * 회원 정보 수정
     */
    @PostMapping("/user/modify")
    public int modifyUser(@RequestBody MemberDto dto) {
        return memberService.updateMember(dto);
    }
    
    /**
     * 로그인 이력 조회 (본인)
     */
    @GetMapping("/loginHistory")
    public ResponseEntity<?> loginHistory(HttpSession session) {
        String id = (String) session.getAttribute("id");
        if (id == null) {
            return ResponseEntity.status(401).body(null);
        }
        return ResponseEntity.ok(loginHistoryService.getUserLoginHistory(id));
    }

    /**
     * 관리자: 전체 로그인 이력 조회
     */
    @GetMapping("/loginHistory/all")
    public ResponseEntity<?> allLoginHistory(HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).body("권한이 없습니다.");
        }
        return ResponseEntity.ok(loginHistoryService.getAllLoginHistory());
    }

    /**
     * 관리자: 전체 사용자별 총 접속시간 통계
     */
    @GetMapping("/loginHistory/stats")
    public ResponseEntity<?> loginStats(HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).body("권한이 없습니다.");
        }
        return ResponseEntity.ok(loginHistoryService.getTotalLoginStats());
    }

    /**
     * 현재 로그인한 사용자 id/role 반환 (header.html용)
     */
    @GetMapping("/whoami")
    public Map<String, Object> whoami(HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", session.getAttribute("id"));
        map.put("role", session.getAttribute("role"));
        System.out.println("[/whoami] 반환 role: " + session.getAttribute("role"));
        return map;
    }
}
