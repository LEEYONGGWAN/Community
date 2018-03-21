package com.ktds.interceptors;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginIntercepter extends HandlerInterceptorAdapter {
	private Map<String, Integer> idFailCountMap;
	//private final Logger logger = Logger.getLogger(LoginIntercepter.class);

	public LoginIntercepter() {
		idFailCountMap = new HashMap<String, Integer>();
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String contextPath = request.getContextPath();

		HttpSession session = request.getSession();
		String id = request.getParameter("id");

		if (request.getMethod().equals("POST")) {
			//logger.debug("post 상태 확인");

			try {
				if (session.getAttribute("status").equals("fail")) {
					//logger.debug("status fail 확인");

					if (idFailCountMap.containsKey(id)) {
						//logger.debug("로그인 실패한 적 있음");
						//logger.debug("로그인 실패한 아이디 : " + id);

						int count = idFailCountMap.get(id);
						idFailCountMap.put(id, count + 1);

						//logger.debug("로그인 실패한 횟수 " + idFailCountMap.get(id));

						if (idFailCountMap.get(id) > 2) {
							//logger.debug("로그인 3회 이상 실패");
							response.sendRedirect(contextPath + "/loginReject");
							return false;
						}

					} else {
						//logger.debug("처음으로 로그인 틀림");
						idFailCountMap.put(id, 1);
					}
				}
			} catch (NullPointerException npe) {
				//logger.debug("npe 발생");
				//logger.debug(npe);

			}

		}
		return true;
	}

}
