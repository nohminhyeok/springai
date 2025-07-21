package com.example.springai.service;

import org.springframework.stereotype.Service;
import com.example.springai.dto.MemberDto;
import com.example.springai.mapper.MemberMapper;

@Service
public class MemberService {
    // 회원 매퍼 DI
    private final MemberMapper memberMapper;
    
    // 생성자
    public MemberService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    // 로그인
    public MemberDto login(String id, String pw) {
        return memberMapper.login(id, pw);
    }

    // 아이디 중복 체크
    public boolean isIdDuplicate(String id) {
        return memberMapper.isIdDuplicate(id) > 0;
    }

    // 회원가입
    public int join(MemberDto dto) {
        return memberMapper.insertMember(dto);
    }

    // 회원 정보 조회
    public MemberDto getMemberinfo(String id) {
        return memberMapper.getMemberinfo(id);
    }

    // 회원 정보 수정
    public int updateMember(MemberDto dto) {
        return memberMapper.updateMember(dto);
    }
}
