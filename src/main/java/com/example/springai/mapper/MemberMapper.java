package com.example.springai.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.springai.dto.MemberDto;

@Mapper
public interface MemberMapper {
    MemberDto getMemberinfo(String id);
    int updateMember(MemberDto dto);
}
