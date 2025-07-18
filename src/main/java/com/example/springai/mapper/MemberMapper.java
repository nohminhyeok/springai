package com.example.springai.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.springai.dto.MemberDto;

@Mapper
public interface MemberMapper {
	// MemberMapper.java
	MemberDto login(@Param("id") String id, @Param("pw") String pw);
    int isIdDuplicate(String id);
    int insertMember(MemberDto dto);
    MemberDto getMemberinfo(String id);
    int updateMember(MemberDto dto);
}
