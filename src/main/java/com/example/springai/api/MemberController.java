package com.example.springai.api;

import com.example.springai.dto.MemberDto;
import com.example.springai.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
public class MemberController {
    private final MemberService memberService;
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
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
}
