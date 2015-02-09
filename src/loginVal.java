

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ValUser
 */
public class loginVal extends HttpServlet {
	private static final long serialVersionUID = 1L; 
	public static Connection conn1 =null;
	public static Statement st =null;
	public static Statement st1 =null;
	public void init() throws ServletException {
		String dbURL2 = "jdbc:postgresql://localhost/cs387";
		String user = "deepanjan";
		String pass = "najnapeed";

		try {
			Class.forName("org.postgresql.Driver");
			conn1 = DriverManager.getConnection(dbURL2, user, pass);
			st = conn1.createStatement();
			st1 = conn1.createStatement();
			System.out.println("init"+conn1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public loginVal() {
		super();
		System.out.println("Shaktiman");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session;
		session = request.getSession();
		String regBool=request.getParameter("SignUp").toString();
		System.out.println(regBool);
		if(regBool.equals("logout")){
			System.out.println("Deepanjan");
			session.setAttribute("Username", null);
			session.invalidate();
			response.sendRedirect("login.jsp");
		}
		else if(regBool.equals("home")){
			System.out.println("please");
			ResultSet rs;
			String strUserId = (String) session.getAttribute("Username");
			try {
				String homenews = "<br><br><div style=\"float:left;margin-left:500px\"><b>News Notifications:</b></div>" +
				"<div style=\"float:top;\">";
				System.out.println(homenews);
				rs = st.executeQuery("select newsfeed.user_id,newsfeed.course_id,news_text,date_stamp,time_stamp from newsfeed,follow where follow.user_id='"+
						strUserId+"' and follow.course_id=newsfeed.course_id order by date_stamp desc,time_stamp desc limit 10;");
				while(rs.next()){
					homenews+= "<div style=\"float:top;margin-top:30px\"><div style=\"float:left;margin-left:125px; width: " +
					"1000px;padding: 25px;text-align: left;font-size: 100%;color:" +
					"black;border: 1px solid navy;background-color:" +
					"#f1ffff\"><div style = \"float:left;width:800px\">"+"<span style=\"font-size: 80%\"><i>by <b>"+rs.getString(1)+
					"</b></i></span><p>"+rs.getString(3)+"</p></div><div style = \"float:left;margin-left:20px\"><div "+
					"style = \"float:top;margin-left:10px\"><i>Course:</i><b>"+rs.getString(2)+"</b></div>"+
					"<div style = \"float:top;margin-top:20px;margin-left:10px\"><i>Date:</i><b>"+rs.getString(4)+"</b></div>"+
					"<div style = \"float:top;margin-top:30px;margin-left:10px\"><i>Time:</i><b>"+rs.getString(5)+"</b></div></div></div>";
				}
				homenews+="</div>";
				session.setAttribute("home_news",homenews);
				response.sendRedirect("home.jsp");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if(regBool.equals("timeline")){
			try{
				ResultSet rs;
				String user = (String) session.getAttribute("Username");
				String results="";
				rs=st.executeQuery("Select course_id,title from student natural join follow natural join course " +
						"where user_id='"+user+"'");
				results+= "	<div style=\"float:left;margin-left:500px\"><br><b>Courses followed:</b></div><br>" +
				"<table border=\"1\" style=\"margin-left:125px\" width=\"82%\"><col style=\"width:40%\">" +
				"<col style=\"width:60%\">" +
				"<thead><tr bgcolor=\"#9999CC\"><th>Course-id</th><th>Course Name</th>" +
				"</tr></thead><tbody bgcolor=\"#66FFCC\">";
				while(rs.next()){
					results+="<tr><td>"+rs.getString(1)+"</td><td>"+rs.getString(2)+"</td></tr>";
				}
				results+="</table><br><br><div style=\"float:left;margin-left:500px\"><b>Your Recent Course Reviews:</b></div>";
				session.setAttribute("courses_followed", results);

				rs=st.executeQuery("Select user_id,review_text,date_stamp,time_stamp from coursereview where user_id='"+
						user+"' order by date_stamp desc, time_stamp desc limit 5;");
				int i=0;
				String review_text="<div style=\"float:top;\">";
				//System.out.println("dfghj "+count);
				while(rs.next()){
					review_text+="<div style=\"float:top;margin-top:30px\"><div style=\"float:left;margin-left:125px; width: " +
					"1000px;padding: 25px;text-align: left;font-size: 100%;color:" +
					"black;border: 1px solid navy;background-color:" +
					"#f1ffff\"><div style = \"float:left;width:800px\">"+"<span style=\"font-size: 80%\"><i>by <b>"+rs.getString(1)+
					"</b></i></span><p>"+rs.getString(2)+"</p></div><div style = \"float:left;margin-left:20px\">"+
					"<div style = \"float:top;margin-top:10px\">Date:"+rs.getString(3)+"</div>"+
					"<div style = \"float:top;margin-top:20px\">Time:"+rs.getString(4)+"</div>"+"</div></div></div>";
				}
				review_text+="</div><br><br><div style=\"float:left;margin-left:500px\"><b>Your Recent Instructor Reviews:</b></div>"+
				"<div style=\"float:top;\">";
				rs=st.executeQuery("Select user_id,review_text,date_stamp,time_stamp from instructorreview where user_id='"+
						user+"' order by date_stamp desc, time_stamp desc limit 5;");
				while(rs.next()){
					review_text+="<div style=\"float:top;margin-top:30px\"><div style=\"float:left;margin-left:125px; width: " +
					"1000px;padding: 25px;text-align: left;font-size: 100%;color:" +
					"black;border: 1px solid navy;background-color:" +
					"#f1ffff\"><div style = \"float:left;width:800px\">"+"<span style=\"font-size: 80%\"><i>by <b>"+rs.getString(1)+
					"</b></i></span><p>"+rs.getString(2)+"</p></div><div style = \"float:left;margin-left:20px\">"+
					"<div style = \"float:top;margin-top:10px\">Date:"+rs.getString(3)+"</div>"+
					"<div style = \"float:top;margin-top:20px\">Time:"+rs.getString(4)+"</div>"+"</div></div></div>";
				}

				review_text+="</div><br><br><div style=\"float:left;margin-left:500px\"><b>Your Recent Material Uploads:</b></div>"+
				"<div style=\"float:top;\">";
				String query = "Select materialname,description,user_id,date_stamp,time_stamp from material where user_id='"+user+
				"' order by date_stamp desc, time_stamp desc limit 5;";	
				rs=st.executeQuery(query);
				while(rs.next()){
					review_text+="<div style=\"float:top;margin-top:30px\"><div style=\"float:left;margin-left:125px; width: " +
					"1000px;padding: 25px;text-align: left;font-size: 100%;color:" +
					"black;border: 1px solid navy;background-color:" +
					"#f1ffff\">"+"<div style=\"float:left;width:800px\"><form method=\"get\" action=\"course\"><input type=\"hidden\" name=\"user\" value=\""+rs.getString(3)+"\"> <input type=\"hidden\" name=\"select_dept\" value=\"retrieveMaterial\">  <input type=\"submit\" name=\"fileName\" style=\"background-color:#6495ed;\" value=\""+rs.getString(1)+"\">" 
					+"<span style=\"font-size:80%\"><i> by <b>"+rs.getString(3)+"</b></i></span><p>"+rs.getString(2)+
					"</p></form></div><div style=\"float:left;margin-left:10px;\">" +
					"<div style = \"float:top;margin-top:10px\">Date:"+rs.getString(4)+"</div>"+
					"<div style = \"float:top;margin-top:20px\">Time:"+rs.getString(5)+"</div>"+
					"</div></div></div><br><br><br>";
					i++;
				}
				review_text+="</div>";

				session.setAttribute("timeline_code", review_text);
				System.out.println(review_text);
				response.sendRedirect("timeline.jsp");
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String regBool = request.getParameter("SignUp").toString();
		String strErrMsg = null;
		HttpSession session = request.getSession();

		if(regBool.equals("login")){
			String strUserId = request.getParameter("UserId").toString();
			String strPassword = request.getParameter("Password").toString();
			boolean isValidLogon = true;
			try {
				try{
					String retval="";
					ResultSet rs;
					rs=st.executeQuery("Select user_id from student where user_id = '"+strUserId+"' and password= '"+strPassword+"'");
					while(rs.next()){
						retval=rs.getString(1);
					}
					System.out.println(retval);
					if(retval.equals("")){
						isValidLogon=false;
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
				if(isValidLogon) {
					session.setAttribute("Username", strUserId);
					session.setAttribute("connection", conn1);
					response.sendRedirect("./loginVal?SignUp=home");
				} else {
					strErrMsg = "User name or Password is invalid. Please try again.";
					session.setAttribute("errorMsg", strErrMsg);
					response.sendRedirect("login.jsp");
				}
			} catch(Exception e) {
				strErrMsg = "Unable to Val user / password in database";
				session.setAttribute("errorMsg", strErrMsg);
				response.sendRedirect("login.jsp");
			}
		}
		else if(regBool.equals("reg"))
		{
			String strUserId = request.getParameter("New UserId").toString();
			String strPassword = request.getParameter("New Password").toString();
			String strRoll = request.getParameter("rollno").toString();
			String strName = request.getParameter("name").toString();
			String strEmail= request.getParameter("email").toString();
			if(strEmail.equals("") || strName.equals("") || strRoll.equals("") ||strUserId.equals("") 
					|| strPassword.equals("")){
				response.sendRedirect("login.jsp");
				return;
			}
				
			try{
				st.executeUpdate("Insert into student values ( '"+strUserId+"','"+strPassword+"','"+
						strRoll+"','"+strName+"','"+strEmail+"');");
				session.setAttribute("Username", strUserId);
				response.sendRedirect("./loginVal?SignUp=home");
			}
			catch(SQLException exception){
				String type=exception.getMessage();
				System.out.println(type);
				session.setAttribute("errorMsg", type);
				response.sendRedirect("login.jsp");
			}
		}	
	}


}
