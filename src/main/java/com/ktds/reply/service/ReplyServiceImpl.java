package com.ktds.reply.service;

import java.util.List;

import com.ktds.reply.dao.ReplyDao;
import com.ktds.reply.vo.ReplyVO;

public class ReplyServiceImpl implements ReplyService{

	private ReplyDao replyDao;
	
	public void setReplyDao(ReplyDao replyDao) {
		this.replyDao = replyDao;
	}
	
	@Override
	public List<ReplyVO> readtAllReplies(int communityId) {
		return replyDao.selectAllReplies(communityId);
	}

	@Override
	public ReplyVO readOneReply(int replyId) {
		return replyDao.selectOneRelpy(replyId);
	}
	
	@Override
	public boolean createReply(ReplyVO replyVO) {
		return replyDao.insertReply(replyVO) >0 ;
	}



}
