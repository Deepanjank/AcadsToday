<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<div style="width:1200px;height:300px">
<div style="float:left; margin-left:100px;width:870px">
<img src="./image.jpg" width="800" height="295" alt="Login">
</div>
 <div style="background-color:black; color:white; margin-left:100px;padding:20px;">
<h2><br><br>ACADS TODAY<br><br></h2>

<p>
Your Helper To a Better Course Selection.
<br>
</p>
<br>
<br>
<br>
</div> 
</div>
<div style="width:1200px;height:300px">
	<div style="float:left; margin-left:100px;width:500px;background-color:#6699FF">
		<div style="margin-left:20px;">
			<form action="createuser" method="post">
				 <H3 >Login</H3>
				 <div style="float:left">
				 	<div style="float:left">
				 		<h4> Username : <br></h4>
				 		<input type="text" name="Username"size="20px">
				 	</div>
				 	<div style="margin-left:200px">
				 		<h4>Password : </h4>
				 		<input type="password" name="Password" size="20px">
				 	</div>
				 </div>
				 <div style="margin-top:80px;margin-left:400px">
				 	<input type="submit" value="Log In">
				 </div>
				 <br>
				 <br>
				 <br>
			 </form>
		</div>
	</div>
	<div style="margin-left:700px; background-color:#FFCC66">
		<div style="margin-left:40px; padding:10px">
			<form action="createuser" method="post">
				 <H1>Sign Up</H1> 
				 <br/>
				 New Username
				 <input type="text" name="New Username"size="20px" style="margin-left:30px">
				 <br>
				 <br>
				 Password
				 <input type="password" name="New Password" size="20px" style="margin-left:68px">
				 <br>
				 <br>
				 Roll Number
				 <input type="text" name="rollno" size="20px" style="margin-left:45px">
				 <br>
				 <br>
				 Name
				 <input type="text" name="name" size="20px" style="margin-left:91px">
				 <br>
				 <br>
				 Department
				 <input type="text" name="dept" size="20px" style="margin-left:53px">
				 <br>
				 <br>
				 Email Id
				 <input type="text" name="email" size="20px" style="margin-left:73px">
				 <br>
				 <br>
				 <input type="submit" value="submit">
			 </form>
		</div>
	</div>
</div>
<br>
<br>
</body>
</html>