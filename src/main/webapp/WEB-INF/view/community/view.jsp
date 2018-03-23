<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${community.title}</title>

<link  rel="stylesheet" type="text/css" href="<c:url value="/static/css/alert.css"/>" />

<script src="<c:url value="/static/js/jquery-3.3.1.min.js"/>" type="text/javascript"></script>

<script type="text/javascript" src="<c:url value="/static/js/alert.js" />"></script>
<script type="text/javascript">

	$().ready(function() {	
		// 여기선 get으로 만 동작하는 에이젝스이기 때문에 겟으로 보낸다
		
		
		loadReplies(0);
		function loadReplies(scrollTop){
		$.get("<c:url value="/api/reply/${community.id}"/>", {}, 
				function(response) {
					for(var i in response){
						appendReplies(response[i]);
					}
					
					//위치 유지 
					$(window).scrollTop(scrollTop);
			});
		}
		
		
		
		
		//ajax 에서 폼형태로 보내주고 싶을때 (멀티파트폼은 에이젝스에서 보내기 불가)
		$("#writeReplyBtn").click(function() {
			$.post("<c:url value="/api/reply/${community.id}" />",
					$("#writeReplyForm").serialize(),
					function(response) {
						if( response.status){
							show("댓글 등록 됨");
							
							$("#parentReplyId").val("0");
							$("#body").val("");
							$("#createReply").appendTo("#createReplyDiv");
							
							var scrollTop = $(window).scrollTop();
							alert(scrollTop);
														
							//appendReplies(response.reply);
							//ID가 replies 인것을 초기화
							$("#replies").html("");
							loadReplies(scrollTop);
						}
						else{
							alert("등록의 실패했습니다. 잠시 후에 다시 시도하세요.");	
						}
					});
		});
		
		$("#replies").on("click", ".re-reply", function() {
			var parentReplyId = $(this).closest(".reply").data("id");
			$("#parentReplyId").val(parentReplyId);
			//appendTo 는 붙여넣는게 아니고 위치를 옮기는것
			$("#createReply").appendTo($(this).closest(".reply"));
		}); 
		
		
		$("#deleteBtn").click(function() {
			$(location).attr("href", "<c:url value="/delete/${community.id}"/>");
		});
		
		
		function appendReplies(reply){
			
			var replyDiv = $("<div class='reply' 
					data-id='"+ reply.id +"' 
					style= 'padding-left:"+ ((reply.level-1) * 20) +"px;'></div>");
			
			var nickname = reply.memberVO.nickname + "(" + reply.memberVO.email +")";
			var top = $("<span class='writer'>" + nickname + "</span><span class='regist-date'>" + reply.registDate + "</span>")
			replyDiv.append( top );
			
			var body = $("<div class ='body'>"+  reply.body + "</div>")
			replyDiv.append( body);
			
			var registReReply = $("<div class= 're-reply'>★댓글 달기★</div>");
			replyDiv.append(registReReply);
			$("#replies").append(replyDiv);
		}
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
			<div id="createReplyDiv">
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