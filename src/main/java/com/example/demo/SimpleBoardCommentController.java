package com.example.demo;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import com.google.gson.Gson;

@Controller
@RequiredArgsConstructor
public class SimpleBoardCommentController {
	
	private final SimpleBoardDataRepository simpleBoardDataRepository;
	private final MemberInfoRepository memberInfoRepository;
	
	
//	@RequestMapping(value="/", method=RequestMethod.GET)
//	public String index(HttpServletRequest request) {
//		List<SimpleBoardData> result = simpleBoardDataRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
//		request.setAttribute("boardList", result);
//		return "index";
//	}
	
//	@RequestMapping(value="/add", method=RequestMethod.POST)
//	public String add(HttpServletRequest request) {
//		String name = request.getParameter("username");
//		String contents = request.getParameter("contents");
//		SimpleBoardData simpleBoard = SimpleBoardData.builder()
//						.name(name)
//						.contents(contents)
//						.build();
//		simpleBoardDataRepository.save(simpleBoard);
//		List<SimpleBoardData> result = simpleBoardDataRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
//		request.setAttribute("boardList", result);
//		return "index";
//	}
	

//	@RequestMapping(value="/addREST", method=RequestMethod.GET)
//	@ResponseBody
//	public String addREST(HttpServletRequest request) {
//		String name = request.getParameter("username");
//		String contents = request.getParameter("contents");
//		String password = request.getParameter("password");
//		SimpleBoardData simpleBoard = SimpleBoardData.builder()
//						.name(name)
//						.contents(contents)
//						.password(password)
//						.build();
//		simpleBoardDataRepository.save(simpleBoard);
//		return "OK";
//	}
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String index(HttpServletRequest request) {
		//List<SimpleBoardData> result = simpleBoardDataRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
		//request.setAttribute("boardList", result);
		return "index_rest";
	}
	
	@RequestMapping(value="/loginREST", method=RequestMethod.POST)
	@ResponseBody
	public String loginREST(HttpServletRequest request, HttpSession session) {
		String userId = request.getParameter("id");  // 입려받은 아이디
		String password = request.getParameter("password");  // 입력받은 패스워드
		System.out.println("들어온 아이디 : " + userId + "들어온 패스워드 : " + password);
		
		// 입력받은 아이디가 DB 있는지 확인
		Long findCount = memberInfoRepository.countByIdAndPassword(userId, password); 
		
		if(findCount <= 0) {  // 없으면
			System.out.println("로그인 실패");
			return "{\"result\" : \"fail\"}";  // {"result" : "fail"}
		}else {  // 있으면
			Optional<MemberInfo> mem = memberInfoRepository.findById(userId);
			if(!mem.isPresent()) return "{\"result\" : \"fail\"}";
			MemberInfo m = mem.get();  // 로그인한 회원의 엔티티 객체
			String userName = m.getName();
			int userGrade = m.getGrade();
			String grade;
			if(userGrade == 0) grade = "member";
			else grade = "admin";
			session.setAttribute("loginok", userId);  // 로그인한 회원의 아이디 세션에 저장
			session.setAttribute("loginname", userName);  // 로그인한 회원의 이름 세션에 저장
			session.setAttribute("membergrade", grade);
			System.out.println("로그인 성공한 아이디 : " + userId);
			System.out.println("로그인 성공한 이 : " + userName);
			System.out.println("로그인 후 세션 값1 : " + session.getAttribute("loginok"));
			System.out.println("로그인 후 세션 값2 : " + session.getAttribute("loginname"));

			return "{\"result\": \"success\", \"grade\": \"" + grade + "\" }";
			// {"result" : "success"}
		}
	}
	
	@RequestMapping(value="/logoutREST", method=RequestMethod.GET)
	@ResponseBody
	public String logoutREST(HttpServletRequest request, HttpSession session) {
		session.setAttribute("loginok", "");  // 로그아웃 버튼 누르면 세션 값 지움
		session.setAttribute("loginname", "");
		return "{\"result\": \"success\"}";
	}
	
	@RequestMapping(value="/addREST", method=RequestMethod.POST)
	@ResponseBody
	public String addREST(HttpServletRequest request, HttpSession session) {
		String name = (String)session.getAttribute("loginname");  // 세션에 저장한 로그인한 회원 이름
		String contents = request.getParameter("contents");  // 입력받은 댓글 내용
		String writer = (String)session.getAttribute("loginok");  // 세션에 저장한 로그인한 회원 아이디
//		String writer = request.getParameter("userId");
		//String password = request.getParameter("password");
		SimpleBoardData simpleBoard = SimpleBoardData.builder()
						 .name(name)
						.contents(contents)
						.writer(writer)
						.build();
		simpleBoardDataRepository.save(simpleBoard);
		return "OK";
	}
	
	@RequestMapping(value="/listREST", method=RequestMethod.GET)
	@ResponseBody
	public String listREST(HttpServletRequest request) {
		// 댓글 저장한 데이터 가져오기
		List<SimpleBoardData> resultList = simpleBoardDataRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
		String json = new Gson().toJson(resultList);  // 가져온 데이터 List를 json 형식으로 변환
		return json;
	}
	
	@RequestMapping(value="/delREST", method=RequestMethod.GET)
	@ResponseBody
	public String delREST(HttpServletRequest request, HttpSession session) {
		
		//String password = request.getParameter("password");
		String writer = (String) session.getAttribute("loginok");  // 세션에 저장한 로그인한 회원 아이디
		String id = request.getParameter("id");  // 게시글 번호
		System.out.println("삭제하려고 받은 게시글 번호 : " + id);
		String memberGrade = (String)session.getAttribute("membergrade");  // 세션에 저장한 등급
		Long contentsId = Long.parseLong(id);  // 게시글 번호를 Long 타입으로 변환
		// 게시글번호와 작성자가 모두 동일한 데이터 찾아 개수 반환
		Long findCount = simpleBoardDataRepository.countByIdAndWriter(contentsId, writer);
		if(findCount > 0 || memberGrade.equals("admin")) {  // 해당 게시글번호데이터가 있거나 관지자이면
			simpleBoardDataRepository.deleteById(contentsId);
			return "{\"result\" : \"success\"}";  // {"result" : "fail"}
		}else {  // 데이터 있으면
			simpleBoardDataRepository.deleteById(contentsId);  // 해당 데이터 삭제
			return "{\"result\": \"fail\"}";  // {"result" : "success"}
		}
	}
	
	@RequestMapping(value="/modREST", method=RequestMethod.POST)
	@ResponseBody
	public String modREST(HttpServletRequest request, HttpSession session) {
		String contents = request.getParameter("contents");
		String idStr = request.getParameter("id");  // 게시글 번호
		String modWriter = request.getParameter("writer");
		String modName = request.getParameter("name");
		Long id = Long.parseLong(idStr);
		System.out.print("수정할 CONTENTS(댓글) = > " + contents + ", ID => " + id);
		SimpleBoardData simpleBoardData = SimpleBoardData.builder()
				.id(id)
				.name(modName)
				.writer(modWriter)
				.contents(contents)
				.build();
		simpleBoardDataRepository.save(simpleBoardData);
		
		return "OK";

	}
	
}