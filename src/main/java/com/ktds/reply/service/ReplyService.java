package com.ktds.reply.service;

import java.util.List;

import com.ktds.reply.vo.ReplyVO;

public interface ReplyService {
	public List<ReplyVO> readtAllReplies(int communityId);
	public boolean createReply(ReplyVO replyVO);
}