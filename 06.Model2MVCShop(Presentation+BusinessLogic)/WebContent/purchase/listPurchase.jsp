<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
    
<% System.out.println("구매목록 jsp 시작 :: ");%>

<html>
<head>
<title>구매 목록조회</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">

<script type="text/javascript">
	function fncList(currentPage) {
		document.getElementById("currentPage").value = currentPage;
	   	document.detailForm.submit();	
	}
	
</script>
</head>

<body bgcolor="#ffffff" text="#000000">

<div style="width: 98%; margin-left: 10px;">

<form name="detailForm" action="/listPurchase.do" method="post">

<table width="100%" height="37" border="0" cellpadding="0"	cellspacing="0">
	<tr>
		<td width="15" height="37"><img src="/images/ct_ttl_img01.gif"width="15" height="37"></td>
		<td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left: 10px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="93%" class="ct_ttl01">구매 목록조회</td>
				</tr>
			</table>
		</td>
		<td width="12" height="37"><img src="/images/ct_ttl_img03.gif"	width="12" height="37"></td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0"	style="margin-top: 10px;">
	<tr>
		<td colspan="14" >
			전체  ${resultPage.totalCount } 건수, 현재  ${resultPage.currentPage} 페이지
		</td>
	</tr>
	<tr>
		<td class="ct_list_b" width="50">No</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="50">구매날짜</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="100">구매번호</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="100">상품번호</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="50">회원ID</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="50">회원명</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">전화번호</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">배송현황</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">정보수정</td>
	</tr>
	<tr>
		<td colspan="14" bgcolor="808285" height="1"></td>
	</tr>
	
	<!-- for문 돌면서 list 사이즈 만큼 뽑음 -->	
	<c:set var="i" value="0" />
		<c:forEach var = "purchase" items="${list}">
			<c:set var ="i" value ="${i+1}"/>

			<tr class="ct_list_pop">
			
				<td align="center">${i}</td>
				<td></td>
				
				<td align="left">${purchase.orderDate}</td>
				<td></td>
				
				<td align="left"><a href="/getPurchase.do?tranNo=${purchase.tranNo}">${purchase.tranNo}</a></td>
				<td></td>
				
				<td align="left">${purchase.purchaseProd.prodNo}</a></td>
				<td></td>
				
				<td align="left">
					<a href="/getUser.do?userId=${purchase.buyer.userId}">${purchase.buyer.userId}</a>
				</td>
				<td></td>
		
				<td align="left">${purchase.receiverName}</td>
				<td></td>
		
				<td align="left">${purchase.receiverPhone}</td>
				<td></td>
				
				<td align="left">
				<!-- 현재상태 항목(배송코드에 따라)  -->	
				<c:choose>
					<c:when test="${purchase.tranCode.trim()=='0'}">구매가 최소되었습니다.</c:when>
					<c:when test="${purchase.tranCode.trim()=='1'}">현재 구매완료 상태입니다.
						<a href="/updateTranCode.do?tranNo=${purchase.tranNo}&tranCode=0">구매취소 </a>
					</c:when>
					<c:when test="${purchase.tranCode.trim()=='2'}">현재 배송중입니다.</c:when>
					<c:when test="${purchase.tranCode.trim()=='3'}">현재 배송완료 상태입니다.
						<a href="/updateTranCode.do?tranNo=${purchase.tranNo}&tranCode=-1">반품신청 </a>
					</c:when>
					<c:when test="${purchase.tranCode.trim()=='-1'}">반품신청이 완료되었습니다. 기다려주세요.</c:when>
					<c:when test="${purchase.tranCode.trim()=='-3'}">반품신청이 불가합니다.죄송합니다.</c:when>
					<c:otherwise>반품 처리가 완료되었습니다.</c:otherwise>
				</c:choose>
				<td></td>
		
				<td align="left">
					<c:if test="${purchase.tranCode.trim()=='2'}">
						<a href="/updateTranCode.do?tranNo=${purchase.tranNo}&tranCode=3">물건도착 </a>
					</c:if>
				</td>
			</tr>
			<tr>
				<td colspan="14" bgcolor="D6D7D6" height="1"></td>
			</tr>

		</c:forEach>	
</table>

<!-- 페이지 처리 -->
<table width="100%" border="0" cellspacing="0" cellpadding="0"	style="margin-top:10px;">
	<tr>
		<td align="center">
		   <input type="hidden" id="currentPage" name="currentPage" value=""/>
			<jsp:include page="../common/pageNavigator.jsp"/>	
    	</td>
	</tr>
</table>
<!--  페이지 Navigator 끝 -->
</form>

</div>

</body>
</html>