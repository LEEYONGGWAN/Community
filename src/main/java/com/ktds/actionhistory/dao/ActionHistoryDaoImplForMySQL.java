package com.ktds.actionhistory.dao;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.ktds.actionhistory.vo.ActionHistorySearchVO;
import com.ktds.actionhistory.vo.ActionHistoryVO;

public class ActionHistoryDaoImplForMySQL extends SqlSessionDaoSupport implements ActionHistoryDao{

	@Override
	public int insertActionHistory(ActionHistoryVO actionHistoryVO) {
		return getSqlSession().insert("ActionHistoryDao.insertActionHistory", actionHistoryVO);
	}


	@Override
	public int selectAllActionHistoryCount(ActionHistorySearchVO actionHistorySearchVO) {
		return getSqlSession().selectOne("ActionHistoryDao.selectAllActionHistoryCount", actionHistorySearchVO);
	}

	@Override
	public List<ActionHistoryVO> selectAllActionHistory(ActionHistorySearchVO actionHistorySearchVO) {
		return getSqlSession().selectList("ActionHistoryDao.selectAllActionHistory", actionHistorySearchVO);
	}



}
