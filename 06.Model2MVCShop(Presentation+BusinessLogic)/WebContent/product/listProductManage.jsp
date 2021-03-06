<%@ page contentType="text/html; charset=euc-kr" pageEncoding="EUC-KR" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%System.out.println("상품목록ㅡ jsp 시작 :: ");%>

<html>
<head>
<title>상품 목록 확인 및 수정</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">

<script type="text/javascript">
	// 검색 / page 두가지 경우 모두 Form 전송을 위해 JavaScrpt 이용  
	function fncList(currentPage) {
		document.getElementById("currentPage").value = currentPage;
	   	document.detailForm.submit();		
	}
</script>
</head>

<body bgcolor="#ffffff" text="#000000">

<div style="width:98%; margin-left:10px;">

<form name="detailForm" action="/product/listProduct?menu=manage" method="post">

	<table width="100%" height="37" border="0" cellpadding="0"	cellspacing="0">
		<tr>
			<td width="15" height="37">
				<img src="/images/ct_ttl_img01.gif" width="15" height="37"/>
			</td>
			<td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left:10px;">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="93%" class="ct_ttl01">
						
								상품 목록 확인 후 수정
						
						</td>
					</tr>
				</table>
			</td>
			<td width="12" height="37">
				<img src="/images/ct_ttl_img03.gif" width="12" height="37"/>
			</td>
		</tr>
	</table>
	
	<!-- 검색 옵션 시작 -->
	
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
			<!-- 컨디션이 널이 아니면 if 문 시작 -->
			<c:if test="${! empty search.searchCondition}">
				<td align="right">
					<select name="searchCondition" class="ct_input_g" style="width:80px">
						<c:choose>
							<c:when test="${search.searchCondition==0}">
								<option value="0" selected>상품번호</option>
								<option value="1">상품명</option>
								<option value="2">상품가격</option>
							</c:when>
							<c:when test="${search.searchCondition==1}">
								<option value="0">상품번호</option>
								<option value="1" selected>상품명</option>
								<option value="2">상품가격</option>
							</c:when>	
							<c:when test="${search.searchCondition==2}">
								<option value="0">상품번호</option>
								<option value="1">상품명</option>
								<option value="2"selected>상품가격</option>
							</c:when>	
						</c:choose>
					</select>
					<input 	type="text" name="searchKeyword"  value="${search.searchKeyword}" 
						class="ct_input_g" style="width:200px; height:19px" >
				</td>
			</c:if>
			<!-- 컨디션이 널이 아니면 if 문 끝 -->
			
			<!-- 컨디션이 널이면 if 문 시작 -->
			<c:if test="${empty search.searchCondition}">
				<td align="right">
					<select name="searchCondition" class="ct_input_g" style="width:80px">
						<option value="0" selected>상품번호</option>
						<option value="1">상품명</option>
						<option value="2">상품가격</option>
					</select>
					<input type="text" name="searchKeyword"  class="ct_input_g" style="width:200px; height:19px" >
				</td>
			
			</c:if> 
			<!-- 컨디션이 널이면 if 문 끝 -->
			
			<td align="right" width="70">
			<!-- 검색 -->
				<table border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="17" height="23">
							<img src="/images/ct_btnbg01.gif" width="17" height="23"/>
						</td>
						<td background="/images/ct_btnbg02.gif" class="ct_btn01" style="padding-top:3px;">
							<a href="javascript:fncList('1');">검색</a>
						</td>
						<td width="14" height="23">
							<img src="/images/ct_btnbg03.gif" width="14" height="23"/>
						</td>
					</tr>
				</table>
			<!-- 검색   끝-->	
			</td>
		</tr>
	</table>
	
	<!-- 검색 옵션  끝-->
	
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
		<tr>
			<td colspan="6" >
				전체  ${resultPage.totalCount } 건수, 현재  ${resultPage.currentPage} 페이지
			</td>
			<td colspan="5" > 
				<a href="/product/listProduct?listOrderby=0&menu=manage">신상품순</a>&nbsp;&nbsp; <a href="/product/listProduct?listOrderby=1&menu=manage">가격낮은순</a>&nbsp;&nbsp;<a href="/product/listProduct?listOrderby=2&menu=manage">가격높은순</a> 
				<input type="hidden" name="listOrderby" value="${search.listOrderby}"/>
			</td>
		</tr>
		<tr>
			<td class="ct_list_b" width="100">No</td>
			<td class="ct_line02"></td>
			<td class="ct_list_b" width="150">상품명</td>
			<td class="ct_line02"></td>
			<td class="ct_list_b" width="150">상품번호</td>
			<td class="ct_line02"></td>
			<td class="ct_list_b" width="150">가격</td>
			<td class="ct_line02"></td>
			<td class="ct_list_b">등록일</td>	
			<td class="ct_line02"></td>
			<td class="ct_list_b">수량</td>
			<td class="ct_line02"></td>	
			<td class="ct_list_b">현재상태</td>
			<td class="ct_line02"></td>	
		</tr>
		<tr>
			<td colspan="11" bgcolor="808285" height="1"></td>
		</tr>
		
	<!-- for문 돌면서 list 사이즈 만큼 뽑음 -->	
	<c:set var="i" value="0" />
		<c:forEach var = "product" items="${list}">
				<c:set var ="i" value ="${i+1}"/>
		<tr class="ct_list_pop">
			<td align="center">${ i }</td>
				<td></td>
				<td align="left"><a href="/product/updateProduct?prodNo=${product.prodNo}&menu=manage">${product.prodName}</a></td>
				
				<td></td>
				<td align="left">${product.prodNo}</td>
				
				
				<td></td>
				<td align="left">${product.price}</td>
				
				<td></td>
				<td align="left">${product.manuDate}</td>
				
				<td></td>
				<td align="left">${product.quantity}</td>
				
				<td></td>
					<c:choose>
						<c:when test="${product.quantity>=1}">
							<td align="left">판매중</td>
						</c:when>	
						<c:otherwise>
							<td align="left">품절(재입고 준비중)</td>
						</c:otherwise>
					</c:choose>
					

		</tr>
		<tr>
			<td colspan="11" bgcolor="D6D7D6" height="1"></td>
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
