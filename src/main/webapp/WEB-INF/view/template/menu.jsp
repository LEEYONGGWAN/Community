<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" type="text/css"
		href="<c:url value="/static/css/link.css"/>">
		<!--  이 jsp는 인터넷 화면 상단에 보이는 화면 -->
<style type="text/css">

#nav>ul {
	padding: 0px;
	margin: 0px;
}

#nav li {
	display: inline-block;
	margin-left: 15px;
}

#nav li:FIRST-CHILD {
	margin-left: 0px;
}
</style>


<div id="nav">
	<ul>
		<!--  만약에 세션스코프에서 유저가 비어있으면 로그인으로 가지게 해라 -->
		<c:if test="${empty sessionScope.__USER__ }">
			<li>
				<a href="<c:url value="/login"/>"> Regist/login </a>
			</li>
		</c:if>


		<!--  만약에 세션스코프에서 유저가 있으면 상단에 닉네임님 뜨게  -->
		<c:if test="${not empty sessionScope.__USER__ }">
			<li>
				(${sessionScope.__USER__.nickname}님) 
				<a href="<c:url value="/logout"/>">Logout</a>
				<div>
					<a href= "<c:url value="/delete/process1/"/> " >    회원 탈퇴 </a>
				</div>
			</li>

		</c:if>

		<li>
			<a href="<c:url value="/"/>">Community</a>
		</li>
		

	</ul>
</div>