package com.ktds.member.dao;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.ktds.member.vo.MemberVO;

public class MemberDaoImplForOracle extends SqlSessionDaoSupport implements MemberDao{


	@Override
	public MemberVO selectMember(MemberVO memberVO) {
		return getSqlSession().selectOne("MemberDao.selectMember", memberVO);
	}
	
	@Override
	public int selectCountMemberNickname(String nickname) {
		return getSqlSession().selectOne("MemberDao.selectCountMemberNickname", nickname);
	}
	
	@Override
	public int selectCountMemberEmail(String email) {
		return getSqlSession().selectOne("MemberDao.selectCountMemberEmail", email);
	}
	
	@Override
	public int insertMember(MemberVO memberVO) {
		return getSqlSession().insert("MemberDao.insertMember",memberVO);
	}

	@Override
	public String selectSalt(String email) {
		return getSqlSession().selectOne("MemberDao.selectSalt", email);
	}
	
	

	@Override
	public int deleteMember(int id) {
		return getSqlSession().delete("MemberDao.deleteMember", id);
	}
	
}
