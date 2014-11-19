

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class course
 */
public class course extends HttpServlet {
	private static final long serialVersionUID = 1L; 
	public static Connection conn1 =null;
	Statement st=null;
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
	public course() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String bool_course=null;
		String retval="";
		try{
			bool_course=request.getParameter("select_dept").toString();
			if(bool_course.equals("course")){
				String department=request.getParameter("Departments");
				ResultSet rs;
				rs=st.executeQuery("Select course_id,title from department natural join course where dept_name='"+department+"';");
				retval="<form action=\"course\" method=\"get\" style=\"float:left;margin-left:100px\"> <div style=\"width:1200px\"><div style=\"float:left; margin-left:200px;width:200px\">Select the course:</div><div style=\"float:left;margin-left:40px;width:300px\"><select name=\"Courses\">\n";
				while(rs.next()){
					System.out.println("Comeon");
					retval+="<option value=\""+rs.getString(1)+" : "+rs.getString(2)+"\">"+rs.getString(1)+" : "+rs.getString(2)+"</option>\n";
				}
				retval+="</select></div><div style=\"margin-left:300px\"><input type=\"submit\" value=\"Select\"><input type=\"hidden\" name=\"select_dept\" value=\"course_page\"></div></div></form>";
				session.setAttribute("courses",retval);
				//System.out.println(retval);
				response.sendRedirect("course.jsp");
			}
			else if(bool_course.equals("course_page")){
				String course_name=(String)request.getParameter("Courses");
				session.setAttribute("course_name",course_name);
				response.sendRedirect("coursereview.jsp");
			}
		}
		catch(NullPointerException e)
		{
			try{
				ResultSet rs;
				rs=st.executeQuery("Select dept_name from department;");
				retval="<select name=\"Departments\">\n";
				while(rs.next()){
					retval+="<option value=\""+rs.getString(1)+"\">"+rs.getString(1)+"</option>\n";
				}
				retval+="</select>";
				session.setAttribute("departments",retval);
				session.setAttribute("courses","");
				response.sendRedirect("course.jsp");
			}
			catch(Exception e1){
				e1.printStackTrace();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
