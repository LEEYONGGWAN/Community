<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${community.title}</title>

<script src="<c:url value="/static/js/jquery-3.3.1.min.js"/>"
	type="text/javascript"></script>
<script type="text/javascript">

	$().ready(function() {	
		//ajax 에서 폼형태로 보내주고 싶을때 (멀티파트폼은 에이젝스에서 보내기 불가)
		$("#writeReplyBtn").click(function() {
			console.log($("#writeReplyForm").serialize());
			
			$.post("<c:url value="/api/reply/${community.id}" />",
					$("#writeReplyForm").serialize(),
					function(response) {
						alert("등록됨");
						console.log(response);
			});
		});
		
		
		$("#deleteBtn").click(function() {
			$(location).attr("href", "<c:url value="/delete/${community.id}"/>");
		});
	});
	
</script>

</head>
<body>
	<div id="wrapper">
			<jsp:include page="/WEB-INF/view/template/menu.jsp" />
			<h1>${community.title}</h1>


			<h3>
				<c:choose>
					<c:when test="${ not empty community.memberVO}">
					${community.memberVO.nickname}(${community.memberVO.email}) ${ community.requestIp }
				</c:when>

					<c:otherwise>
					탈퇴한 회원
				</c:otherwise>
				</c:choose>
			</h3>


			<p>${community.viewCount}| ${community.recommendCount} |
				${community.writeDate}</p>
			<p></p>

			<c:if test="${not empty community.displayFilename }">
				<p>
					<a href="<c:url value="/get/${community.id}" />"> ${ community.displayFilename }
					</a>
				</p>
			</c:if>

			<p>${community.body}</p>
			<hr />
			<div id="replies"></div>
			<div id="createReply">
				<form id="writeReplyForm"> 
					<input type="hidden" id="parentReplyId" name="parentReplyId" value="0" />
					<div>
						<textarea id="body" name="body"></textarea>
					</div>
					<div>
						<input type="button" id="writeReplyBtn" value="등록" />
					</div>
				</form>
			</div>

			<p>
				<a href="<c:url value="/"/>">뒤로가기</a> <a
					href="<c:url value="/recommend/${community.id}"/>"> 추천하기 </a>
				<c:if test="${ sessionScope.__USER__.id == community.memberVO.id }">
					<a href="<c:url value ="/modify/${ community.id }"/>">수정하기</a>
					<input type="button" id="deleteBtn" value="삭제하기" />
				</c:if>
			</p>
	</div>
</body>
</html>