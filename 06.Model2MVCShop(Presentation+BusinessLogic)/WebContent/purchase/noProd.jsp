<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>��ǰ�ʰ�</title>

<script type="text/javascript">

 function goBack() {
	window.history.back();
}

</script>

</head>
<body>
 ���� ������ ������ �ʰ��Ͽ����ϴ�. ������ �ٽ� �������ּ���
 <div>��ǰ�̸� : ${product.prodName} </div>
  <div>���Ű����� ���� : ${product.quantity}</div>
  <div>������ ������ ���� : ${purchase.quantity }</div>
  <input type="button" onclick="goBack();" value="�ٽ� ������������ ����"></button>
</body>
</html>