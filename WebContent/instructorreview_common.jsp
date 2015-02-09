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
<br><br><br>
<div style="float:left;margin-left:125px; width: 1000px;padding: 25px;text-align: center;font-size: 200%;color:#68be23;border: 1px solid navy;background-color:#ccffff">
<b><%=session.getAttribute("instructor_name") %></b>
</div>

<br>
<div align="center" style="float:top;margin-top: 100px">
	<strong> Average Rating </strong> <%=session.getAttribute("instructor_rating")%>
</div>

<div align="center" style="float:top;margin-top: 10px;margin-left :500px">
	<div style="float:left;margin-left: 10px">
	<form action="instructor" method="get">
		<input type="image" src=<%=session.getAttribute("firststar")%> alt="Submit" width="48" height="48">
		<input type="hidden" name="select_dept" value="rating">
		<input type="hidden" name="val" value="1">
	</form>
	</div>
	<div style="float:left">
	<form action="instructor" method="get">
		<input type="image" src=<%=session.getAttribute("secondstar")%> alt="Submit" width="48" height="48">
		<input type="hidden" name="val" value="2">
		<input type="hidden" name="select_dept" value="rating">
	</form>
	</div>
	<div style="float:left">
	<form action="instructor" method="get">
		<input type="image" src=<%=session.getAttribute("thirdstar")%> alt="Submit" width="48" height="48">
		<input type="hidden" name="select_dept" value="rating">
		<input type="hidden" name="val" value="3">
	</form>
	</div>
	<div style="float:left">
	<form action="instructor" method="get">
		<input type="image" src=<%=session.getAttribute("fourthstar")%> alt="Submit" width="48" height="48">
		<input type="hidden" name="select_dept" value="rating">
		<input type="hidden" name="val" value="4">
	</form>
	</div>
	<div style="float:left">
	<form action="instructor" method="get">
		<input type="image" src=<%=session.getAttribute("fifthstar")%> alt="Submit" width="48" height="48">
		<input type="hidden" name="select_dept" value="rating">
		<input type="hidden" name="val" value="5">
	</form>
	</div>
</div>
<div style="float:top;margin-top:100px;">
<div style="float:left;margin-left:450px;">
<form action="instructor" method="get">
<input type="submit" value="Instructor Reviews" style="background-color: #6495ed";" >
<input type="hidden" name="select_dept" value="instructor_page">
<input type="hidden" name="instructors" value="123456">
</form>
</div>
<div style="float:left;margin-left: 90px;width:500px">
<input type="button" value="Add a Review" style="background-color: #6495ed"; onClick="javascript:window.location='addinstructorreview.jsp';">
</div>
</div>
<br><br>
</body>
</html>