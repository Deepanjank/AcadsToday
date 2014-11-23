

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
 * Servlet implementation class ValidateUser
 */
public class ValidateUser extends HttpServlet {
	private static final long serialVersionUID = 1L; 
	public static Connection conn1 =null;
	public static Statement st =null;
	public void init() throws ServletException {
		String dbURL2 = "jdbc:postgresql://localhost/cs387";
		String user = "deepanjan";
		String pass = "najnapeed";

		try {
			Class.forName("org.postgresql.Driver");
			conn1 = DriverManager.getConnection(dbURL2, user, pass);
			st = conn1.createStatement();
			System.out.println("init"+conn1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ValidateUser() {
		super();
		System.out.println("Shaktiman");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session;
		session = request.getSession();

		try{
			String regBool=request.getParameter("SignUp").toString();

			if(regBool.equals("logout")){
				System.out.println("Deepanjan");
				session.setAttribute("Username", null);
				session.invalidate();
				response.sendRedirect("login.jsp");
			}
			/*else if(regBool.equals("timeline")){
			response.sendRedirect("timeline.jsp");
		}*/
		}
		catch(NullPointerException e){
			String homenews = "<div style=\"float:left; margin-left:100px\"><p> <b>News Feed: </b><br>";
			ResultSet rs;
			String strUserId = (String) session.getAttribute("Username");
			try {
				rs = st.executeQuery("select course_id,news_text,time_stamp from newsfeed where user_id='"+
						strUserId+"';");
				while(rs.next()){
					homenews+= "<br>"+rs.getString(1) + " &nbsp; &nbsp; " + rs.getString(2) + "&nbsp; &nbsp; " +
					rs.getString(3);
				}
				homenews+="</div>";
				session.setAttribute("home_news",homenews);
				response.sendRedirect("home.jsp");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String regBool = request.getParameter("SignUp").toString();
		//String timeline = request.getParameter("timeline").toString();
		String strErrMsg = null;
		HttpSession session = request.getSession();

		if(regBool.equals("login")){
			String strUserId = request.getParameter("UserId").toString();
			String strPassword = request.getParameter("Password").toString();
			boolean isValidLogon = true;
			try {
				ResultSet rs;

				try{
					String retval="";
					rs=st.executeQuery("Select user_id from student where user_id = '"+strUserId+"' and password= '"
							+strPassword+"'");
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

					String homenews = "<div style=\"float:left; margin-left:100px\"><p> <b>News Feed: </b><br>";
					rs = st.executeQuery("select course_id,news_text,time_stamp from newsfeed where user_id='"+
							strUserId+"';");
					while(rs.next()){
						homenews+= "<br>"+rs.getString(1) + " &nbsp; &nbsp; " + rs.getString(2) + "&nbsp; &nbsp; " +
						rs.getString(3);
					}
					homenews+="</div>";
					session.setAttribute("home_news",homenews);
					response.sendRedirect("home.jsp");
				} else {
					strErrMsg = "User name or Password is invalid. Please try again.";
					session.setAttribute("errorMsg", strErrMsg);
					response.sendRedirect("login.jsp");
				}
			} catch(Exception e) {
				strErrMsg = "Unable to validate user / password in database";
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
			String strDeptId= request.getParameter("dept").toString();
			String strEmail= request.getParameter("email").toString();
			try{
				st.executeUpdate("Insert into student values ( '"+strUserId+"','"+strPassword+"','"+
						strRoll+"','"+strName+"','"+strDeptId+"','"+strEmail+"');");
				session.setAttribute("Username", strUserId);
				String followed = "<div style=\"float:left; margin-left:100px\"> <b>Courses followed:</b>";
				String homenews = "<div style=\"float:left; margin-left:100px\"><p> <b>News Feed: </b><br>";
				session.setAttribute("courses_followed",followed);
				session.setAttribute("home_news",homenews);
				response.sendRedirect("home.jsp");
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
