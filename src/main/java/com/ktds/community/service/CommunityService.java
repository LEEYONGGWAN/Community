package com.ktds.community.service;

import java.util.List;

import com.ktds.community.vo.CommunitySearchVO;
import com.ktds.community.vo.CommunityVO;

import io.github.seccoding.web.pager.explorer.PageExplorer;

public interface CommunityService {
	public PageExplorer getAll(CommunitySearchVO communitySearchVO);
	
	public CommunityVO getOne(int id);

	public int readMyCommunitiesCount(int userId);

	public List<CommunityVO> readMyCommunities(int userId);
	
	public boolean createCommunity(CommunityVO communityVO);
	
	public boolean increaseR(int id);
	
	public boolean increaseV(int id);
	
	public boolean removeOne(int id);
	
	public boolean deleteCommunities(List<Integer> ids, int userId);
	
	public boolean updateCommunity(CommunityVO communityVO);
}
