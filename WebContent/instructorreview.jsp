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
<jsp:include page="./instructorreview_common.jsp" />
<%=session.getAttribute("instructor_review_code") %>


<form action="instructor" method="get" >
<div style="float:left; margin-left:130px;">
<input type="submit" value="See More" style="background-color:#6495ed; ">
</div>
<input type="hidden" name="select_dept" value="seeMoreReview">
<input type="hidden" name="instructors" value="aaaaaaa">
</form>
<div style="float:bottom ;">
<br />&nbsp;<br />&nbsp;<br />&nbsp;
</div>
</body>
</html>