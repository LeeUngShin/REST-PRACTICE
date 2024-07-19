<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<HTML>
	<head>
		<meta charset="utf8">
	</head>
	<body onload="loginCheck()">  <!-- 페이지 로드 시 로그인 체크-->
		<script>
			
			// 세션은 리프레쉬 되어야 사용가능 즉, 동일 페이지에서 데이터만 받으면 세션 데이터는 뷰로 안온다...
			// 그래서 새로고침 하기 전까지는 세션과 동일한 데이터를 사용하려면
			// json 형태로 반환값에 포함해서 데이터로 받아야 한다
			// return "{\"result\": \"success\", \"grade\": \"" + grade + "\" }";
			// 위의 반환을 받으면 { "result" : "success"
			//					"grade" : "member" } 형태로 온다.
			// 글머에도 세션값을 받아오는 이유는 새로고침 시에 사용하려고???
			var loginok = '<%=(String)session.getAttribute("loginok")%>';
			var member_grade = '<%=(String)session.getAttribute("membergrade")%>';

			function loginCheck(){
				console.log(loginok);
				console.log(member_grade);
				//var loginok = '<%=(String)session.getAttribute("loginok")%>'; 
				if(loginok != "" && loginok != "null"){  // 로그인 상태이면
					document.getElementById("loginbox").style.display = "none";
					document.getElementById("contentsdiv").style.display = "block";
					
					getList();
				}
				else{  // 비로그인 상태이면
					document.getElementById("loginbox").style.display = "block";
					document.getElementById("contentsdiv").style.display = "none";
					document.getElementById("userid").focus();	
				}
			}
			
			function goLogin(){  // 로그인 버튼 누르면 이 함수 호출
				var userId = document.getElementById("userid").value;  // 입력한 id
				var password = document.getElementById("userpassword").value;  // 입력한 pw
				document.getElementById("userid").value = "";
				document.getElementById("userpassword").value = "";
				const payload = new FormData();  // 입력한 데이터를 서버로 보낼 객체
				payload.append("id", userId);  // 객체에 데이터 넣기
				payload.append("password", password);  // 객체에 데이터 넣기
				fetch("http://127.0.0.1:8080/loginREST",{  // 로그인 버튼 누르면 이 url로 매핑
					method: "POST",  // 메서드
					body : payload,  // 서버로 보낼 객체
				})
				.then((response) => response.json())  // 반환 받은 데이터를 json 타입으로 
				.then((data) =>{  // json으로 변환한 데이터를 data로
					document.getElementById("userid").focus();	
					if(data.result == "fail"){  // data.키 = 값
						alert("로그인 실패");
					}else{
						loginok = userid;
						member_grade = data.grade;
						document.getElementById("loginbox").style.display="none";
						document.getElementById("contentsdiv").style.display="block";
						getList();
					}
				});
			}

			function goLogout() {  // 로그아웃 버튼 누르면 이 함수 호출
				fetch("http://127.0.0.1:8080/logoutREST")  // 로그아웃 버튼 누르면 이 url로 매핑
				.then((response) => {
					loginok = "";
					member_grade = "";
					document.getElementById("loginbox").style.display = "block";
					document.getElementById("contentsdiv").style.display = "none";		
					document.getElementById("userid").focus();	    
				}); 		
			}
			
			/*
			// GET 메서드의 경우
			function goAdd() {
				var user = document.getElementById("username").value;
				var contents = document.getElementById("contents").value;
				fetch("http://127.0.0.1:8080/addREST?username=" + user + "&contents=" + contents)
				.then((response) => {
				  document.getElementById("username").value = "";
				  document.getElementById("contents").value = "";
				  getList();
				});
			}
			*/

			// POST 메서드의 경우
			function goAdd() {  // 댓글 작성 후 입력버튼 누르면 이 함수 호출
				// var username = document.getElementById("username").value;
				//var username = document.getElementById("loginname").value;
				var contents = document.getElementById("contents").value;  // 입력한 댓글 내용
				//var password = document.getElementById("password").value;
				//<var userId = '<%=(String)session.getAttribute("loginok")%>';
				//var username = '<%=(String)session.getAttribute("loginname")%>';

				const payload = new FormData();
				//payload.append("username", username);
				payload.append("contents", contents);
				//payload.append("userId", userId);
				//payload.append("password", password);
				fetch("http://127.0.0.1:8080/addREST",{  // 입력버튼 누르면 이 url로 매핑
							method : "POST",  // 메서드
							body: payload,  // 보낼 데이터 객체
				})
				.then((response) => {
				  //document.getElementById("username").value = "";  
				  document.getElementById("contents").value = "";  // 입력 칸 비움
				  //document.getElementById("password").value = "";
				  getList();  // 댓글 리스트 뿌려주는 함수
				});
			}

			function getList() {  // 댓글 리스트 가져오기
				fetch("http://127.0.0.1:8080/listREST")  // 로그인하거나 댓글 입력하면 이 url로 매핑
					.then((response) => response.json())  // 가져온 댓글 List를 json 타입으로 변환
					.then((data) => {  // json으로 변환된 댓글List
					  document.getElementById("contentsTable").innerHTML = 
						"<tr>" +
						"	<td width=50>ID</td> " +
						"	<td width=100>작성자</td> " +
						"	<td width=200>내용</td>" +
						"</tr>";
					//var loginok = '<%=(String)session.getAttribute("loginok")%>'; 
					for(index = 0; index < data.length; index++) {  // 댓글 개수만큼 반복
						var delText = "";
						var loginok = '<%=(String)session.getAttribute("loginok")%>';
						if(data[index].writer == loginok || member_grade == "admin") {  // 게시글의 작성자와 현재 로그인한 회원의 아이디가 같으면

							// 현재 게시글의 게시글번호를 매개변수로 하는 goDel호출하는 <a>태그를 delText에 저장(삭제버튼)
							// => 내가 작성한 댓글에 대해서만 삭제버튼 나오도록
							// 이동할 함수에 매개변수로 현재 게시글의 번호 보냄
							delText = "<a href='javascript:goDel(" + data[index].id + ")'>[X]</a>";
						}
						
						document.getElementById("contentsTable").innerHTML += 
									"		<tr><td>" + delText + " " + data[index].id + "</td>" +
									"			<td>" + data[index].name + "</td>" +
									"			<td>" + data[index].contents + "</td>" +
									"		</tr>";
						
					}
					  document.getElementById("contents").focus();  // 댓글 입력하는 칸으로 포커싱
					});
			}

			function goDel(id) {  // 댓글의 삭제버튼을 누르면 이 함수 호출, 게시글번호 받음
				if(confirm("정말로 삭제하겠습니까")){
					// result = window.prompt("비밀번호 입력", "");
					// fetch("http://127.0.0.1:8080/delREST?id=" + id + "&password=" + result)
					fetch("http://127.0.0.1:8080/delREST?id=" + id)  // 아이디는 서버쪽 컨트롤러에서도 가져올 수 있음
					.then((response) => response.json())
					.then((data) => {
						if(data.result == "fail"){  // json으로 변환한 데이터.키 = 값
							alert("비밀번호가 틀렸습니다.");
						}
						else{
					 		getList();
						}
					});
				}
			}

			
			function keyPress(event, menunum){
				if(event.keyCode == 13){  // 엔터키 누르면
					if(menunum ==1){
						goAdd();  // 댓글 저장 함수로
					}
					else{
						goLogin();
					}
				}
			}
							
		</script>
		
		<%String name = (String)session.getAttribute("loginok") %>
		${name}님 로그인 중<br>
		${loginok}님 로그인 중<br>
		
		
		<div id="loginbox">
			<!--<form name="loginForm" method="POST">-->
				<input type="text" name="id" id="userid"><br>
				<input type="password" name="password" id="userpassword" onKeydown="javascript:keyPress(event, 2)"><br>
				<input type="button" value="로그인" onclick="javascript:goLogin()"><br>
			<!--</form>-->
		</div>

		<div id="contentsdiv">
			<table>
				<tr>
					<td colspan=3>
						<!--<form id="myForm" method="POST">-->
							<!--<input type="text" id="username" name="username" placeholder="이름">-->
							<input type="text" id="contents" name="contents" placeholder="내용" onKeydown="javascript:keyPress(event, 1)">
							<!--<input type="text" id="password" name="password" 
									onkeydown="javascript:keyPress(event)" placeholder="비밀번호"> -->
							<input type="button" value="입력" onclick="javascript:goAdd()">
							<input type="button" value="로그아웃" onclick="javascript:goLogout()">
						<!--</form>-->
					</td>
				</tr>
			</table>
			
			<table id="contentsTable">
			</table>
		</div>
	</body>	
</HTML>
