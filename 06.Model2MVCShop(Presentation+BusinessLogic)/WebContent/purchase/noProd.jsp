<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>상품초과</title>

<script type="text/javascript">

 function goBack() {
	window.history.back();
}

</script>

</head>
<body>
 구매 가능한 수량을 초과하였습니다. 수량을 다시 선택해주세요
 <div>상품이름 : ${product.prodName} </div>
  <div>구매가능한 수량 : ${product.quantity}</div>
  <div>고객님이 선택한 수량 : ${purchase.quantity }</div>
  <input type="button" onclick="goBack();" value="다시 구매페이지로 가기"></button>
</body>
</html>