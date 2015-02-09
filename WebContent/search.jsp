<%@page import="java.sql.Connection"%>
<%@page import="java.sql.ResultSet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>AcadsToday</title>
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
<form action="Search" method="post">
<div>
<%=session.getAttribute("search_aid") %>
<div style="float:left;margin-left:400px">
<input class="required" type="text" name="search" height="30px" width="100px" size="20px" style="margin-left:73px" >
</div>
<div style="float:left;">
<input type="image" src="./search.jpeg" alt="Submit Form" height="40px" width="40px">
</div>
</div>
</form>
</body>
</html>