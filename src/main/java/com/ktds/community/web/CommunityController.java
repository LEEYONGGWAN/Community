package com.ktds.community.web;

import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ktds.actionhistory.vo.ActionHistory;
import com.ktds.actionhistory.vo.ActionHistoryVO;
import com.ktds.community.service.CommunityService;
import com.ktds.community.vo.CommunitySearchVO;
import com.ktds.community.vo.CommunityVO;
import com.ktds.member.constants.Member;
import com.ktds.member.vo.MemberVO;
import com.ktds.util.DownloadUtil;

import io.github.seccoding.web.pager.explorer.PageExplorer;

@Controller
public class CommunityController {

	private CommunityService communityService;
	//private final Logger logger = LoggerFactory.getLogger(CommunityController.class);

	public void setCommunityService(CommunityService communityService) {
		this.communityService = communityService;
	}

	@RequestMapping("/reset")
	public String viewInitListPage(HttpSession session) {
		
		session.removeAttribute("__SEARCH__");
		return "redirect:/";
	
	}
	
	@RequestMapping("/")
	public ModelAndView viewListPage(CommunitySearchVO communitySearchVO,  HttpSession session ) {

		
		// 데이터가 안넘어왔을 경우
		// 1. 리스트페이지에 처음 접근했을 때
		// 2. 글 내용을 보고, 목록보기 링크를 클릭했을 때
		if( communitySearchVO.getPageNo() < 0) {
			// Session 에 저장된 CommunitySearchVO 를 가져옴.
			communitySearchVO = (CommunitySearchVO)session.getAttribute("__SEARCH__");
			// Session 에 저장된 CommunitySearchVO 가 없을 경우, PageNo=0 으로 초기화
			if( communitySearchVO == null ) {
				communitySearchVO = new CommunitySearchVO();
				communitySearchVO.setPageNo(0);
			}
		}

		// 위에서 작업한것 세션에 넣어주기
		session.setAttribute("__SEARCH__", communitySearchVO);
		
		/*
		 * if (session.getAttribute(Member.USER) == null) { return new
		 * ModelAndView("redirect:/login"); }
		 */

		ModelAndView view = new ModelAndView();
		// /WEB-INF/view/community/list.jsp
		view.setViewName("community/list");

		//내가 뭐로 검색했는지 알려주는거임
		view.addObject("search", communitySearchVO);
		
		PageExplorer pageExplorer = communityService.getAll(communitySearchVO);
		view.addObject("pageExplorer", pageExplorer);

		return view;
	}

	@RequestMapping(value = "/write", method = RequestMethod.GET)
	public String viewWritePage() {
		/*
		 * if (session.getAttribute(Member.USER) == null) { return "redirect:/login"; }
		 */

		return "community/write";
	}

	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public ModelAndView doWrite(@ModelAttribute("writeForm") @Valid CommunityVO communityVO, Errors errors,
			HttpSession session, HttpServletRequest request) {
		//logger.info(request.getRequestURI() + "write 사용중");
		/*
		 * /////////////////////////////////////////////////////////////////////////////
		 * //////// if(communityVO.getTitle() == null || communityVO.getTitle().length()
		 * == 0) { session.setAttribute("status", "emptyTitle"); return
		 * "redirect:/write"; }
		 * 
		 * if(communityVO.getBody() ==null || communityVO.getBody().length() ==0) {
		 * session.setAttribute("status", "emptyBody"); return "redirect:/write"; }
		 * 
		 * if(communityVO.getWriteDate() == null ||
		 * communityVO.getWriteDate().length()==0) { session.setAttribute("status",
		 * "emptyDate"); return "redirect:/write"; }
		 * 
		 * /////////////////////////////////////////////////////////////////////////////
		 */
		
		// 로그인을 하지않은 상태에서 접근하려하지않을때(ex; 글작성시간이 30분이상 되서 세션이 자동 로그아웃된 상태)
		communityVO.save();

		if (session.getAttribute(Member.USER) == null) {
			return new ModelAndView("redirect:/login");
		}

		if (errors.hasErrors()) {
			ModelAndView view = new ModelAndView();
			view.setViewName("community/write");
			view.addObject("communityVO", communityVO);
			return view;
		}

		String requestIp = request.getRemoteAddr();
		communityVO.setRequestIp(requestIp);

		boolean isSuccess = communityService.createCommunity(communityVO);

		// 만약에 글쓰기 등록이 완료 되었다면~ 리스트로 정보 보내고 다시 가겠다.
		if (isSuccess) {
			return new ModelAndView("redirect:/reset");
		}

		return new ModelAndView("redirect:/write");
	}

	@RequestMapping("/view/{id}")
	public ModelAndView viewViewPage(/* HttpSession session, */ @PathVariable int id) {

		/*
		 * if (session.getAttribute(Member.USER) == null) { return new
		 * ModelAndView("redirect:/login"); }
		 */

		ModelAndView view = new ModelAndView();
		view.setViewName("community/view");

		CommunityVO community = communityService.getOne(id);

		view.addObject("community", community);

		return view;
	}

	@RequestMapping("/read/{id}")
	public String viewCount(@PathVariable int id) {

		if (communityService.increaseV(id)) {
			return "redirect:/view/" + id;
		}
		return "redirect:/";

	}

	@RequestMapping("/recommend/{id}")
	public String rCount(@PathVariable int id,
						  @RequestAttribute ActionHistoryVO actionHistory) {
		actionHistory.setReqType(ActionHistory.ReqType.COMMUNITY);
		String log = String.format(ActionHistory.Log.RECOMMEND, id);
		actionHistory.setLog(log);

		if (communityService.increaseR(id)) {
			return "redirect:/view/" + id;
		}
		return "redirect:/";
	}

	@RequestMapping("/get/{id}")
	public void download(@PathVariable int id, 
			HttpServletRequest request, 
			HttpServletResponse response,
			@RequestAttribute ActionHistoryVO actionHistory)
	{
		CommunityVO community = communityService.getOne(id);
		String filename = community.getDisplayFilename();
	
		actionHistory.setReqType(ActionHistory.ReqType.MEMBER);
		String log = String.format(ActionHistory.Log.DOWNLOAD, id);
		actionHistory.setLog(log);
		
		
		DownloadUtil download = new DownloadUtil("D:\\uploadFiles/" + filename);
		try {
			download.download(request, response, filename);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

	}

	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable int id, HttpSession session, @RequestAttribute ActionHistoryVO actionHistory) {
		
		actionHistory.setReqType(ActionHistory.ReqType.COMMUNITY);
		String log = String.format(ActionHistory.Log.DELETE, id);
		actionHistory.setLog(log);
		
		

		MemberVO member = (MemberVO) session.getAttribute(Member.USER);

		CommunityVO community = communityService.getOne(id);

		boolean isMyCommunity = member.getId() == community.getUserId();

		if (isMyCommunity && communityService.removeOne(id)) {
			return "redirect:/";
		}
		return "/WEB-INF/view/error/404";
	}

	@RequestMapping(value = "/modify/{id}", method=RequestMethod.GET)
	public ModelAndView viewModifyPage(@PathVariable int id, HttpSession session) {
		

		MemberVO member = (MemberVO) session.getAttribute(Member.USER);
		CommunityVO community = communityService.getOne(id);

		int userId = member.getId();

		if (userId != community.getUserId()) {
			return new ModelAndView("error/404");
		}
		
		ModelAndView view = new ModelAndView();
		view.setViewName("community/write");
		view.addObject("communityVO", community);
		view.addObject("mode", "modify");
		return view;
	}
	
	
	@RequestMapping(value = "/modify/{id}", method=RequestMethod.POST)
	public String doModifyAction(@PathVariable int id,
									HttpSession session,
									HttpServletRequest request,
									@ModelAttribute("writeForm") @Valid CommunityVO communityVO,
									Errors errors,
									@RequestAttribute ActionHistoryVO actionHistory) {
		
		
		MemberVO member = (MemberVO)session.getAttribute(Member.USER);
		CommunityVO originalVO = communityService.getOne(id);
		
		if( member.getId() != originalVO.getUserId() ) {
			return "error/404";
		}
		
		if( errors.hasErrors()) {
			return "redirect:/modify/" + id;
		}
		
		CommunityVO newCommunity = new CommunityVO();
		newCommunity.setId( originalVO.getId() );
		newCommunity.setUserId( member.getId() );
		
		boolean isModify =false;
		
		String asIs = "";
		String toBe = "";
		
		
		
		// 1.IP 변경확인
		String ip = request.getRemoteAddr();
		if( !ip.equals(originalVO.getRequestIp())) {
			//달라진 ip만 newCommunity 여기에 넣어준다
			newCommunity.setRequestIp(ip);
			isModify =true;
			asIs +="IP :" + originalVO.getRequestIp() + "<br/>";
			toBe +="IP :" + ip + "<br/>";
		}
		
		//2. 제목 변경확인
		if( !originalVO.getTitle().equals( communityVO.getTitle())) {
			newCommunity.setTitle(communityVO.getTitle());
			isModify =true;
			asIs +="Title :" + originalVO.getTitle() + "<br/>";
			toBe +="Title :" + communityVO.getTitle() + "<br/>";
			
		}
		
		//3. 내용 변경확인
		if( !originalVO.getBody().equals( communityVO.getBody())) {
			newCommunity.setBody(communityVO.getBody());
			isModify =true;
			asIs +="Body :" + originalVO.getBody() + "<br/>";
			toBe +="Body :" + communityVO.getBody() + "<br/>";
			
		}
		
		//4. 파일 변경확인
		//아래 이프문은 데이터가 있는지 없는지 알려줌 데이터의 이름의 길이로 해서 널이면 당연히 0임
		if( communityVO.getDisplayFilename().length() >0) {
			File file = new File("D:\\uploadFiles" + communityVO.getDisplayFilename());
			file.delete();
			communityVO.setDisplayFilename("");
		}
		else {
			communityVO.setDisplayFilename(originalVO.getDisplayFilename());
		}
		
		communityVO.save();
		
		if(!originalVO.getDisplayFilename().equals(communityVO.getDisplayFilename() )) {
				newCommunity.setDisplayFilename( communityVO.getDisplayFilename() );
				isModify = true;
				
				asIs +="File :" + originalVO.getDisplayFilename() + "<br/>";
				toBe +="File :" + communityVO.getDisplayFilename() + "<br/>";
			
		}
		
		
		actionHistory.setReqType(ActionHistory.ReqType.COMMUNITY);
		String log = String.format(ActionHistory.Log.UPDATE, 
						originalVO.getBody(), 
						originalVO.getTitle(), 
						originalVO.getBody());
		actionHistory.setLog(log);
		actionHistory.setAsIs(asIs);
		actionHistory.setToBe(toBe);
		
		//5. 변경이 없는지 확인
		if( isModify) {
			//6. update 하는 service code 호출
			communityService.updateCommunity(newCommunity);
		}
		
		return "redirect:/view/" +id;
	}

	
}
