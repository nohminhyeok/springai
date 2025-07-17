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

    public MemberDto getMemberinfo(String id) {
        return memberMapper.getMemberinfo(id);
    }

    public int updateMember(MemberDto dto) {
        return memberMapper.updateMember(dto);
    }
}
