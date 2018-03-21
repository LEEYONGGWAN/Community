package com.ktds.member.dao;

import com.ktds.member.vo.MemberVO;

public interface MemberDao {
	
	public int selectCountMemberEmail(String email);
	
	public int selectCountMemberNickname(String nickname);
		
	public int insertMember(MemberVO memberVO);
	
	public String selectSalt(String email);
	
	public MemberVO selectMember(MemberVO memberVO);
	
	public int deleteMember(int id);
}
