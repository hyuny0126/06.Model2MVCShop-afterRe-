<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html; charset=EUC-KR" %>

<html>
<head>

<title>열어본 상품 보기</title>

</head>
<body>
	당신이 열어본 상품을 알고 있다
<br>
<br>
<%
	request.setCharacterEncoding("euc-kr");
	response.setCharacterEncoding("euc-kr");
	String history = null;
	
	
	Cookie[] cookies = request.getCookies();
	
	
	if (cookies!=null && cookies.length > 0) {
		
		System.out.println("쿠키창안1111111");
		
		for (int i = 0; i < cookies.length; i++) {
			
			Cookie cookie = cookies[i];
			
			System.out.println("쿠키창안2222222222"+cookie);

			if (cookie.getName().equals("history")) {
				history = cookie.getValue();
				System.out.println("쿠키창안3333333333"+history);
			}
		}
		if (history != null) {
			String[] h = history.split(",");
			for (int i = 0; i < h.length; i++) {
				if (!h[i].equals("null")) {
%>
<a href="/product/getProduct?prodNo=<%=h[i]%>&menu=search"
	target="rightFrame"><%=h[i]%></a>
<br>
<%
				}
			}
		}
	}
%>

</body>
</html>