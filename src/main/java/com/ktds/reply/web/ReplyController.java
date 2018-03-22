package com.ktds.reply.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ktds.member.constants.Member;
import com.ktds.member.vo.MemberVO;
import com.ktds.reply.service.ReplyService;
import com.ktds.reply.vo.ReplyVO;

@Controller
public class ReplyController {
	
	private ReplyService replyService;
	
	public void setReplyService(ReplyService replyService) {
		this.replyService = replyService;
	}
	
	@RequestMapping(value = "/api/reply/{communityId}", method=RequestMethod.GET)
	@ResponseBody
	public List<ReplyVO> getAllReplies(@PathVariable int communityId) {
		return replyService.readtAllReplies(communityId);
		
	}
	
	//어떤글에 쓰는지 누가쓰는지 파라미터 순서대로 알려줌
	@RequestMapping(value = "/api/reply/{communityId}", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> createReply(@PathVariable int communityId,
														  HttpSession session,
														  ReplyVO replyVO){
	
		MemberVO member = (MemberVO) session.getAttribute(Member.USER);	
		replyVO.setUserId(member.getId());
		replyVO.setCommunityId(communityId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		boolean isSuccess = replyService.createReply(replyVO);
		result.put("status", isSuccess);
		result.put("reply", replyVO);
		return result;
	}
	
	
}
