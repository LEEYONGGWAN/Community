package com.ktds.community.web;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ktds.community.service.CommunityService;
import com.ktds.community.vo.CommunityVO;
import com.ktds.member.constants.Member;
import com.ktds.member.vo.MemberVO;
import com.ktds.util.DownloadUtil;

@Controller
public class CommunityController {

	private CommunityService communityService;
	//private final Logger logger = LoggerFactory.getLogger(CommunityController.class);

	public void setCommunityService(CommunityService communityService) {
		this.communityService = communityService;
	}

	@RequestMapping("/")
	public ModelAndView viewListPage(/* HttpSession session */) {
		/*
		 * if (session.getAttribute(Member.USER) == null) { return new
		 * ModelAndView("redirect:/login"); }
		 */

		ModelAndView view = new ModelAndView();

		// /WEB-INF/view/community/list.jsp
		view.setViewName("community/list");

		List<CommunityVO> communityList = communityService.getAll();
		view.addObject("communityList", communityList);

		return view;
	}

	@RequestMapping(value = "/write", method = RequestMethod.GET)
	// @GetMapping("/write")
	public String viewWritePage() {
		/*
		 * if (session.getAttribute(Member.USER) == null) { return "redirect:/login"; }
		 */

		return "community/write";
	}

	@RequestMapping(value = "/write", method = RequestMethod.POST)
	// @PostMapping("/write")
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
			return new ModelAndView("redirect:/");
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
	public String rCount(@PathVariable int id) {

		if (communityService.increaseR(id)) {
			return "redirect:/view/" + id;
		}
		return "redirect:/";
	}

	@RequestMapping("/get/{id}")
	public void download(@PathVariable int id, HttpServletRequest request, HttpServletResponse response) {
		CommunityVO community = communityService.getOne(id);
		String filename = community.getDisplayFilename();
		DownloadUtil download = new DownloadUtil("D:\\uploadFiles/" + filename);
		try {
			download.download(request, response, filename);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

	}

	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable int id, HttpSession session) {

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
									Errors errors) {
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
		
		
		// 1.IP 변경확인
		String ip = request.getRemoteAddr();
		if( !ip.equals(originalVO.getRequestIp())) {
			//달라진 ip만 newCommunity 여기에 넣어준다
			newCommunity.setRequestIp(ip);
			isModify =true;
		}
		
		//2. 제목 변경확인
		if( !originalVO.getTitle().equals( communityVO.getTitle())) {
			newCommunity.setTitle(communityVO.getTitle());
			isModify =true;
		}
		
		//3. 내용 변경확인
		if( !originalVO.getBody().equals( communityVO.getBody())) {
			newCommunity.setBody(communityVO.getBody());
			isModify =true;
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
			
		}
		
		//5. 변경이 없는지 확인
		if( isModify) {
			//6. update 하는 service code 호출
			communityService.updateCommunity(newCommunity);
		}
		
		return "redirect:/view/" +id;
	}

	
}
