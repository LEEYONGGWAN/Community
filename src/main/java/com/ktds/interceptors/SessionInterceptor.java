package com.ktds.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ktds.member.constants.Member;

public class SessionInterceptor extends HandlerInterceptorAdapter {

	//현재 클라스의 이름.class로 매개변수 넣는다
	
	//private final Logger logger = LoggerFactory.getLogger(SessionInterceptor.class);
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object controller)
			throws Exception {
		//현재 프로젝트 경로
		String contextPath = request.getContextPath();

		if (request.getSession().getAttribute(Member.USER) == null) {
			
			//logger.info(request.getRequestURI()+ "안돼, 돌아가.");
			response.sendRedirect(contextPath + "/login");
			
			// 이때는 controller에게 안가고 다시 브라우저로 가는건데 위에 respon.sendRedirect통해 다시 로그인 페이지로 간다.
			return false;
			
		}
		// 리턴트루 일때만 위에 매개변수 controller에게 갈수있다.
		return true;
	}
	
	
	
	
	
	

}
