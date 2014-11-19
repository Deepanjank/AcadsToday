<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<% 
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Cache-Control", "no-store");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);
if(session.getAttribute("Username")==null){response.sendRedirect("login.jsp");}
%>
</head>
<body>

<jsp:include page="./Intro.jsp" />
<form action="Review" method="get" style="float:left;margin-left:100px">
<div style="margin-left:200px">
Enter your Review:<br><br>
</div>
<div style="margin-left:200px">
<input type="text" name="Review" size="20px" style="width: 600px;height:300px">
<br>
<br>
<input type="submit" value="Submit Review">
</div>
</form>
</body>
</html>