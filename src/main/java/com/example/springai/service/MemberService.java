package com.example.springai.service;

import org.springframework.stereotype.Service;
import com.example.springai.dto.MemberDto;
import com.example.springai.mapper.MemberMapper;

@Service
public class MemberService {
    private final MemberMapper memberMapper;
    
    public MemberService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    public MemberDto login(String id, String pw) {
        return memberMapper.login(id, pw);
    }

    public boolean isIdDuplicate(String id) {
        return memberMapper.isIdDuplicate(id) > 0;
    }

    public int join(MemberDto dto) {
        return memberMapper.insertMember(dto);
    }

    public MemberDto getMemberinfo(String id) {
        return memberMapper.getMemberinfo(id);
    }

    public int updateMember(MemberDto dto) {
        return memberMapper.updateMember(dto);
    }
}
