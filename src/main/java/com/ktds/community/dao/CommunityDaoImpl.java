package com.ktds.community.dao;

import java.util.ArrayList;
import java.util.List;

import com.ktds.community.vo.CommunitySearchVO;
import com.ktds.community.vo.CommunityVO;

public class CommunityDaoImpl implements CommunityDao {

	private List<CommunityVO> communityList;

	public CommunityDaoImpl() {
		communityList = new ArrayList<CommunityVO>();
	}


	@Override
	public int insertCommunity(CommunityVO communityVO) {
		communityVO.setId(communityList.size() + 1);
		communityList.add(communityVO);
		return 1;
	}

	@Override
	public CommunityVO selectOne(int id) {
		for(CommunityVO community : communityList) {
			if(community.getId() == id) {
				
				//아래 코드(incrementViewCount)랑 똑같은 내용
//				int vc = community.getViewCount()+1;
//				community.setViewCount(vc);
				return community;
			}
		}
		return null;
	}

	@Override
	public int incrementViewCount(int id) {
		for(CommunityVO community : communityList) {
			if(community.getId() == id) {
				community.setViewCount( community.getViewCount() + 1 );
				return 1;
			}
		}
		return 0;
	}

	@Override
	public int incrementRCount(int id) {
		for(CommunityVO community : communityList) {
			if(community.getId() == id) {
				community.setRecommendCount(community.getRecommendCount() + 1);
				return 1;
			}
		}
		
		return 0;
	}

	@Override
	public int deleteOne(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteMyCommunities(int userId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateCommunity(CommunityVO communityVO) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int selectMyCommunitiesCount(int userId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<CommunityVO> selectMyCommunities(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteCommunities(List<Integer> ids, int userId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List selectAll(CommunitySearchVO communitySerachVO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int selectCountAll(CommunitySearchVO communitySerachVO) {
		// TODO Auto-generated method stub
		return 0;
	}





	
	

}
