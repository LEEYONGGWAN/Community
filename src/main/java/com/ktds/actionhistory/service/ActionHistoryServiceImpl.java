package com.ktds.actionhistory.service;

import com.ktds.actionhistory.dao.ActionHistoryDao;
import com.ktds.actionhistory.vo.ActionHistoryVO;

public class ActionHistoryServiceImpl implements ActionHistoryService{
	private ActionHistoryDao actionHistoryDao;

	public void setActionHistoryDao(ActionHistoryDao actionHistoryDao) {
		this.actionHistoryDao = actionHistoryDao;
	}

	@Override
	public boolean createActionHistory(ActionHistoryVO actionHistoryVO) {
		return actionHistoryDao.insertActionHistory(actionHistoryVO) >0;
	}
}
