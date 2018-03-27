package com.ktds.member.web;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ktds.actionhistory.vo.ActionHistory;
import com.ktds.actionhistory.vo.ActionHistoryVO;
import com.ktds.community.service.CommunityService;
import com.ktds.member.constants.Member;
import com.ktds.member.service.MemberService;
import com.ktds.member.vo.MemberVO;

@Controller
public class MemberController {

	private MemberService memberService;
	private CommunityService communityService;
	
	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}

	public void setCommunityService(CommunityService communityService) {
		this.communityService = communityService;
	}
	
	//ajax reponseBody 어노테이션은 객체 리터럴 형식으로 보내줌(ex attr(action method)) 이것을 json 이라 부른다. 
	//리스폰스바디 쓰려면 jackson-databind 추가 해줘야함   		<type>bundle</type> 그리고 이거 삭제
	
	@RequestMapping("/api/exists/email")
	@ResponseBody
	public Map<String, Boolean> apiIsExistsEmail(@RequestParam String email){
		
		boolean isExists = memberService.readCountMemberEmail(email);
		
		Map<String, Boolean> response = new HashMap<String, Boolean>();
		response.put("response", isExists);
		return response;
		
	}
	
	
	@RequestMapping("/api/exists/nickname")
	@ResponseBody
	public Map<String, Boolean> apiIsExistsNickname(@RequestParam String nickname){
		
		boolean isExists = memberService.readCountMemberNickname(nickname);
		System.out.println(isExists);
		
		Map<String, Boolean> response = new HashMap<String, Boolean>();
		response.put("response", isExists);
		return response;
		
	}
	
	
	
	
	
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String viewLoginPage(HttpSession session) {

		if (session.getAttribute(Member.USER) != null) {
			return "redirect:/";
		}
		return "member/login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String doLoginAction(MemberVO memberVO,HttpSession session, HttpServletRequest request) {
		
		session = request.getSession();

		
		//mysql 이런걸 시도했었다는 내용 로그남기기
		ActionHistoryVO history = (ActionHistoryVO)request.getAttribute("actionHistory");
		history.setReqType(ActionHistory.ReqType.MEMBER);
		String log = String.format(ActionHistory.Log.LOGIN, memberVO.getEmail());
		history.setLog(log);
	
		
		
		// DB에 계정이 존재하지 않을 경우로 변경
		// db에 있는 정보에서 멤버를 찾아서 로그인멤버에 넣어줌 만약에 디비에 아무것도 없으면 null값임
		MemberVO loginMember = memberService.readMember(memberVO);
		
		if (loginMember != null) {
			session.setAttribute(Member.USER, loginMember);
			return "redirect:/";
		}

		return "redirect:/login";

	}

	@RequestMapping("/logout")
	public String doLogoutAction(HttpSession session,
									@RequestAttribute ActionHistoryVO actionHistory) {
		
		MemberVO member = (MemberVO) session.getAttribute(Member.USER);
		
		actionHistory.setReqType(ActionHistory.ReqType.MEMBER);
		String log = String.format(ActionHistory.Log.LOGOUT, member.getEmail());
		actionHistory.setLog(log);
		
		// 세션 소멸
		session.invalidate();
		

		return "redirect:/login";
	}

	
	
	@RequestMapping(value = "/regist", method = RequestMethod.GET)
	public String viewRegistPage() {

		return "member/regist";
	}

	@RequestMapping(value = "/regist", method = RequestMethod.POST)
	public String doRegistAction(@ModelAttribute("registForm") 
										@Valid MemberVO memberVO, Errors errors, 
										HttpServletRequest request, Model model) {

		if ( errors.hasErrors() ) {
			return "member/regist";
		}
		
		ActionHistoryVO history = (ActionHistoryVO) request.getAttribute("actionHistory");
		//ActionHistory 인터페이스 변수에 접근하기
		history.setReqType(ActionHistory.ReqType.MEMBER);
		
		if ( memberService.createMember(memberVO) ) {
			//로그인 성공여부 로그남기기(mysql)
			String log = String.format(ActionHistory.Log.REGIST, memberVO.getEmail(), memberVO.getNickname(), "true");
			history.setLog(log);
			model.addAttribute("actionHistory", history);
			
			
			return "redirect:/login";
		}
		
		//로그인 실패여부 로그남기기(mysql)
		String log = String.format(ActionHistory.Log.REGIST, memberVO.getEmail(), memberVO.getNickname(), "false");
		history.setLog(log);
		model.addAttribute("actionHistory", history);
		
		return "member/regist";
	}
	
	
	
	
	
	@RequestMapping("/delete/process1")
	public String viewVerifyPage() {
		return "member/delete/process1";
	}
	
	
	
	
	@RequestMapping("/delete/process2")
	public ModelAndView viewDeleteMyCommunitiesPage(@RequestParam(required=false, defaultValue = "") String password
													, HttpSession session) {
		
		if( password.length() == 0 ) {
			return new ModelAndView("error/404");
		}
		
		System.out.println("여기들어옴");
		
		MemberVO member = (MemberVO) session.getAttribute(Member.USER);
		member.setPassword(password);

		MemberVO verifyMember = memberService.readMember(member);
		if(verifyMember == null) {
			return new ModelAndView("redirect:/delete/process1");
		}
		//TODO 내가 작성한 게시글의 개수 가져오기
		int myCommunitiesCount = communityService.readMyCommunitiesCount(verifyMember.getId());
		
		
		ModelAndView view = new ModelAndView();
		view.setViewName("member/delete/process2");
		view.addObject("myCommunitiesCount", myCommunitiesCount);
		
		
		//난수값을 만든다음 token 이라는 이름으로 집어넣어라(process3 으로 넘어갈때 중간들에 들어오는애들 못들어오)		
		String uuid = UUID.randomUUID().toString();
		session.setAttribute("__TOKEN__", uuid);
		
		view.addObject("token", uuid);
		
		return view;
	}
	
	@RequestMapping("/account/delete/{deleteFlag}")
	public String removeId(HttpSession session,
							@RequestParam(required=false, defaultValue="") String token,
							@PathVariable String deleteFlag)  {

		String sessionToken = (String) session.getAttribute("__TOKEN__");
		System.out.println(sessionToken);
		if( sessionToken == null || !sessionToken.equals(token)) {
			return "error/404";
		}
		
		
		MemberVO member = (MemberVO) session.getAttribute(Member.USER);

		if (member == null) {
			return "redirect:/login";
		}

		int id = member.getId();

		if (memberService.removeMember(id, deleteFlag)) {
			session.invalidate();
		}
		return "member/delete/delete";
	}

}
