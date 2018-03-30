<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<!--  value 의 /static 은 경로명 아니고 그이후부터가 경로명임 -->
<script src="<c:url value="/static/js/jquery-3.3.1.min.js"/>"	type="text/javascript"></script>
<script type="text/javascript">
	$().ready(function() {

		<c:if test="${mode == 'modify' && not empty communityVO.displayFilename}">
			$("#file").closest("div").hide();
		</c:if>
		
		
		$("#displayFilename").change(function() {
			var isChecked = $(this).prop("checked");
			
			if(isChecked){
				$("label[for=displayFilename]").css({
					"text-decoration-line": "line-through",
					"text-decoration-style": "double",
					"text-decoration-color": "#FF0000"
					
				});
				$("#file").closest("div").show();
			}
			else{
				$("label[for=displayFilename]").css({
					"text-decoration": "none"
				});
				$("#file").closest("div").hide();
			}
		});
		
		
		
		
		
		$("#writeBtn").click(function() {

			
			//현재페이지가 어딘지 알려준다. 내가 모디파이면 모디파이라고 알려주고 이런식
			var mode= "${mode}";
			if( mode == "modify" ){
				var url = "<c:url value="/modify/${communityVO.id}"/>";
			}
			else {
				var url = "<c:url value="/write"/>"
			}
			
			
			var writeForm = $("#writeForm");
			writeForm.attr({
				"method" : "post",
				"action" : url

			});
	
			writeForm.submit();
		});

	});
</script>


</head>
<body>
	
	<div id="wrapper">
		<jsp:include page="/WEB-INF/view/template/menu.jsp"/>
		<form:form modelAttribute="writeForm" enctype="multipart/form-data">

			<div>
				제목 : <input type="text" id="title" name="title" placeholder="제목"
					value="${communityVO.title}" />
			</div>

			<div>
				<form:errors path="title" />
			</div>

			<div id="errorTitle" style="display: none;">제목을 입력하세요!</div>

			<div>
				내용 :
				<textarea id="body" 
						  name="body" 
						  placeholder="내용" 
						  value=" ${communityVO.body}"></textarea>
			</div>

			<c:if test="${ mode == 'modify' && not empty communityVO.displayFilename }">
				<div>
					<input type="checkbox"
							id= "displayFilename"
							name= "displayFilename"
							value= "${ communityVO.displayFilename }"/>
				
					<label for="displayFilename">
						 ${ communityVO.displayFilename } 
					</label>
					
							
				</div>
			</c:if>

			<div>
				<input type="hidden" id="userId" name="userId"
					value="${sessionScope.__USER__.id}" placeholder="아이디" />
			</div>



			<div>
				<input type="file" id="file" name="file" />
			</div>

			<div>
				<input type="button" id="writeBtn" value="등록" />
			</div>
		</form:form>


	</div>


</body>
</html>


