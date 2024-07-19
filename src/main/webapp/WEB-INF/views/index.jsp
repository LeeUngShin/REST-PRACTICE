<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<HTML>
	<body>
	
		<table>
			<tr>
				<td>ID</td>
				<td>작성자</td>
				<td>내용</td>
			</tr>
			<tr>
				<td>1</td>
				<td>홍길동</td>
				<td>하이하이하이~!</td>
			</tr>
			<tr>
				<td colspan=3>
					<form method="POST" action="/add">
						<input type="text" name="name" placeholder="이름">
						<c:if test="${not empty result.fieldError('name')}">
						     <div class="error">${result.fieldError('name').defaultMessage}</div>
						 </c:if>
						<input type="text" name="contents" placeholder="내용">
						<input type="submit" value="입력">
					</form>
				</td>
			</tr>
			<c:forEach items="${list}" var="item">
			<tr>
				<td>${item.id}</td>
				<td>${item.name}</td>
				<td>${item.contents}</td>
			</tr>
			</c:forEach>
		</table>
	</body>	
</HTML>