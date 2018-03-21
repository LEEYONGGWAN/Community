package com.ktds.interceptors;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class IpBlockInterceptor extends HandlerInterceptorAdapter {

	private List<String> blackList;

	public IpBlockInterceptor() {
		blackList = new ArrayList<String>();
		blackList.add("192.168.201.25");
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object controller) throws Exception {

		String ip = request.getRemoteAddr();
		if (blackList.contains(ip)) {
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/view/error/yb.jsp");
			rd.forward(request, response);
			return false;
		}

		return true;

	}

}
