package com.ktds.community.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.ktds.community.vo.CommunityVO;

public class CommunityDaoImplForOracle extends SqlSessionDaoSupport implements CommunityDao{
	
	/*SqlSessionDaoSupport 얘가
	sqlSessionTemplate BEAN 얘를 가지고 있음
	
	지금 여기서는 mybatis가 dao에 접근하게 해주는 기능을 넣어주는 클래스임*/
	
	@Override
	public List<CommunityVO> selectAll() {
		//파라미터는 인터페이스. 메소드명
		return getSqlSession().selectList("CommunityDao.selectAll");
	}
	@Override
	public CommunityVO selectOne(int id) {
		return getSqlSession().selectOne("CommunityDao.selectOne", id);
	}

	@Override
	public int insertCommunity(CommunityVO communityVO) {
		return getSqlSession().insert("CommunityDao.insertCommunity", communityVO);
	}


	@Override
	public int incrementViewCount(int id) {
		return getSqlSession().update("CommunityDao.incrementViewCount" , id);
	}

	@Override
	public int incrementRCount(int id) {
		return getSqlSession().update("CommunityDao.incrementRCount" , id);
	}
	@Override
	public int deleteOne(int id) {
		return getSqlSession().delete("CommunityDao.deleteOne" , id);
	}
	
	@Override
	public int deleteCommunities(List<Integer> ids, int userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
		params.put("userId", userId);
		return getSqlSession().delete("CommunityDao.deleteCommunities", params);
	}
	
	@Override
	public int deleteMyCommunities(int userId) {
		return 0;
	}
	@Override
	public int updateCommunity(CommunityVO communityVO) {
		return getSqlSession().update("CommunityDao.updateCommunity", communityVO);
	}
	@Override
	public int selectMyCommunitiesCount(int userId) {
		return getSqlSession().selectOne("CommunityDao.selectMyCommunitiesCount", userId);
	}
	@Override
	public List<CommunityVO> selectMyCommunities(int userId) {
		
		return getSqlSession().selectList("CommunityDao.selectMyCommunities", userId);
	}

	

	
	
}
