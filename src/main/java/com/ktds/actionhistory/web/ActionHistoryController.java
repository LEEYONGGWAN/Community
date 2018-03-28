package com.ktds.actionhistory.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ktds.actionhistory.service.ActionHistoryService;
import com.ktds.actionhistory.vo.ActionHistorySearchVO;
import com.ktds.actionhistory.vo.ActionHistoryVO;
import com.ktds.util.DateUtil;

import io.github.seccoding.web.pager.explorer.PageExplorer;

@Controller
public class ActionHistoryController {
	
	private ActionHistoryService actionHistoryService;

	private Logger logger = LoggerFactory.getLogger("io.github");
	
	public void setActionHistoryService(ActionHistoryService actionHistoryService) {
		this.actionHistoryService = actionHistoryService;
	}

	
	
	////////////actionHistory.jsp 에서 기간 검색 할때 초기 데이터 설정하는 거임//////
	@RequestMapping("/admin/actionhistory")
	public ModelAndView viewActionHistoryPage(ActionHistorySearchVO actionHistorySearchVO,HttpSession session ) {
		ModelAndView view = new ModelAndView();
		view.setViewName("actionHistory/actionHistory");
		
		
		if( actionHistorySearchVO.getPageNo() < 0){
			actionHistorySearchVO = (ActionHistorySearchVO)session.getAttribute("__AH_SEARCH__");
			
			if(actionHistorySearchVO == null) {
				
				actionHistorySearchVO = new ActionHistorySearchVO();

				actionHistorySearchVO.setPageNo(0);
				//검색날짜 초기화
				setInitiateSearchData(view, actionHistorySearchVO);
			}
		}
		
		actionHistorySearchVO.setStartDate(
				DateUtil.makeDate(actionHistorySearchVO.getStartDateYear()
						, actionHistorySearchVO.getStartDateMonth()
						, actionHistorySearchVO.getStartDateDate())
				//위아래에 얘내 추가하는 이유는 검색시 하루 전날짜로 검색되는 거때문에
				+ " 00:00:00"
				);
		
		actionHistorySearchVO.setEndDate(
				DateUtil.makeDate(actionHistorySearchVO.getEndDateYear()
						, actionHistorySearchVO.getEndDateMonth()
						, actionHistorySearchVO.getEndDateDate())
				+ " 23:59:59"
				);
		
		
		view.addObject("search", actionHistorySearchVO);
		
		int startDateMaximumDate = DateUtil.getActualMaximumDate(
				Integer.parseInt(actionHistorySearchVO.getStartDateYear()),
				Integer.parseInt(actionHistorySearchVO.getStartDateMonth()));
	
		int endDateMaximumDate = DateUtil.getActualMaximumDate(
				Integer.parseInt(actionHistorySearchVO.getEndDateYear()),
				Integer.parseInt(actionHistorySearchVO.getEndDateMonth()));
		
		view.addObject("startDateMaximumDate", startDateMaximumDate);
		view.addObject("endDateMaximumDate", endDateMaximumDate);
		
		session.setAttribute("__AH_SEARCH__",	 actionHistorySearchVO);
		
		System.out.println(ActionHistoryVO.class);
		
		PageExplorer explorer = actionHistoryService.readAllActionHistory(actionHistorySearchVO);
		view.addObject("explorer", explorer);
		
		return view;
	}
	
	
	
	private void setInitiateSearchData(ModelAndView view, ActionHistorySearchVO actionHistorySearchVO) {
		//검색날자 초기화
		String endDate = DateUtil.getNowDate();
		String[] splitedEndDate = endDate.split("-");
		actionHistorySearchVO.setEndDateYear(splitedEndDate[0]);
		actionHistorySearchVO.setEndDateMonth(splitedEndDate[1]);
		actionHistorySearchVO.setEndDateDate(splitedEndDate[2]);
		actionHistorySearchVO.setEndDate(endDate);
		
		String startDate = DateUtil.getComputedDate(
				Integer.parseInt(splitedEndDate[0]),
				Integer.parseInt(splitedEndDate[1]), 
				Integer.parseInt(splitedEndDate[2]),
				DateUtil.DATE, -7);	
		
		String[] splitedStartDate = startDate.split("-");
		actionHistorySearchVO.setStartDateYear(splitedStartDate[0]);
		actionHistorySearchVO.setStartDateMonth(splitedStartDate[1]);
		actionHistorySearchVO.setStartDateDate(splitedStartDate[2]);
		actionHistorySearchVO.setStartDate(startDate);
		
		view.addObject("search", actionHistorySearchVO);
		
		int startDateMaximumDate = DateUtil.getActualMaximumDate(
				Integer.parseInt(splitedStartDate[0]),
				Integer.parseInt(splitedStartDate[1]));
	
		int endDateMaximumDate = DateUtil.getActualMaximumDate(
				Integer.parseInt(splitedEndDate[0]),
				Integer.parseInt(splitedEndDate[1]));
		
		view.addObject("startDateMaximumDate", startDateMaximumDate);
		view.addObject("endDateMaximumDate", endDateMaximumDate);
	}
	
	
	//@repsonseobdy 는리턴 타입이 모델엔뷰랑 스트링이 아닐떄 쓴다
	@RequestMapping("/api/date/max/{year}/{month}")
	public void getMaxDate(@PathVariable int year,
			@PathVariable int month, HttpServletResponse response) {
		int maxDate = DateUtil.getActualMaximumDate(year, month);
		
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.write(maxDate + "");
			out.flush();
		} catch (IOException e) {
			//익셉션과 런타임익셉션의 차이는 에러의 현상을 복구할수 있다 없다.
			//런타임익셉션은 미리 예방 해서 복구할수있지만 익셉션은 불가함
			// 그러기에 여기서는 런타임 익셉션으로 던져주는거임
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			if(out != null) {
				// 얘는 그냥 트라이에서 해주면 안됨 스트림과 관련된 애들은 close 를 무조건 써줘야 되는데
				//이때 finally에서 동작해야됨
				out.close();
			}
		}
	}
	
	
	
}
