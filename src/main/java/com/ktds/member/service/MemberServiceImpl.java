package com.ktds.member.service;

import com.ktds.community.dao.CommunityDao;
import com.ktds.member.dao.MemberDao;
import com.ktds.member.vo.MemberVO;
import com.ktds.util.SHA256Util;

public class MemberServiceImpl implements MemberService{

	private MemberDao memberDao;
	private CommunityDao communityDao;
	
	public void setCommunityDao(CommunityDao communityDao) {
		this.communityDao = communityDao;
	}
	
	public void setMemberDao(MemberDao memberDao) {
		this.memberDao = memberDao;
	}
	
	@Override
	public boolean createMember(MemberVO memberVO) {
		
		//암호화 무작위 생성
		String salt = SHA256Util.generateSalt();
		memberVO.setSalt(salt);
		
		String password = memberVO.getPassword();
		password = SHA256Util.getEncrypt(password, salt);
		memberVO.setPassword(password);
		
		
		return memberDao.insertMember(memberVO) > 0;
	}

	@Override
	public MemberVO readMember(MemberVO memberVO) {
	
		// 1. 사용자의 ID로 SALT  가져오기
		String salt = memberDao.selectSalt(memberVO.getEmail());
		if( salt == null) {
			salt ="";
		}
		
		// 2. SALT 암호화 하기
		String password = memberVO.getPassword();
		password = SHA256Util.getEncrypt(password, salt);
		memberVO.setPassword(password);
		
		return memberDao.selectMember(memberVO);
	}

	@Override
	public boolean removeMember(int id, String deleteFlag) {
		if(deleteFlag.equals("y") ) {
			communityDao.deleteMyCommunities(id);
		}		
		return memberDao.deleteMember(id) > 0;
	}

	@Override
	public boolean readCountMemberEmail(String email) {
		return memberDao.selectCountMemberEmail(email) > 0;
	}

	@Override
	public boolean readCountMemberNickname(String nickname) {
		return memberDao.selectCountMemberNickname(nickname) > 0;
	}
	
}
