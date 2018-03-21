package com.ktds.member.service;

import com.ktds.member.vo.MemberVO;

public interface MemberService {
	
	public boolean readCountMemberEmail(String email);
	
	public boolean readCountMemberNickname(String nickname);
	
	public boolean createMember(MemberVO member);
	
	public MemberVO readMember(MemberVO member);
	
	public boolean removeMember(int id, String deleteFlag);
	
}
