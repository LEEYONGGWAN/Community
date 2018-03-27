package com.ktds.interceptors;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ktds.actionhistory.service.ActionHistoryService;
import com.ktds.actionhistory.vo.ActionHistory;
import com.ktds.actionhistory.vo.ActionHistoryVO;
import com.ktds.member.constants.Member;
import com.ktds.member.vo.MemberVO;

public class PassInterceptor extends HandlerInterceptorAdapter{
	
	private ActionHistoryService actionHistoryService;
	private ActionHistoryVO history2;
	
	
	
	public void setActionHistoryService(ActionHistoryService actionHistoryService) {
		this.actionHistoryService = actionHistoryService;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		HttpSession session = request.getSession();
		MemberVO member = (MemberVO)session.getAttribute(Member.USER);
		
		if( member == null ){
			member = new MemberVO();
		}
		
		ActionHistoryVO history = new ActionHistoryVO();
		history.setReqType(ActionHistory.ReqType.VIEW);
		history.setIp(request.getRemoteAddr());
		history.setUserId(member.getId());
		history.setEmail(member.getEmail());
		
		// "View : %s, Method : %s"  저기에 들어갈 값들을 여기서 하나하나 적어준다.
		String log = String.format(ActionHistory.Log.VIEW, 
					request.getRequestURI(), request.getMethod());
		history.setLog(log);
		
		
		actionHistoryService.createActionHistory(history);
		
		// Controller 에게 ip를 포함한 ActionHistoryVO전달
		history2 = new ActionHistoryVO();
		history2.setIp(request.getRemoteAddr());
		history2.setUserId(member.getId());
		history2.setEmail(member.getEmail());
		request.setAttribute("actionHistory", history2);
		return true;
	}
	
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//모델값만 끄집어 내기
		
		if( modelAndView == null) {
			//ctionHistoryService.createActionHistory(history);
			return;
		}

		if( history2 != null && history2.getReqType() != null)  {
			actionHistoryService.createActionHistory(history2);
		}
			
		
		
		
		}
	
}
