

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
 * Servlet implementation class Search
 */
public class Search extends HttpServlet {
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

	public Search() {
		super();
		System.out.println("Shaktiman");
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
		String type=request.getParameter("type");
		session.setAttribute("searchresults","");
		if(type.equals("students")){
			session.setAttribute("search_type","students");
			response.sendRedirect("search.jsp");
		}
		else if(type.equals("newsfeed")){
			session.setAttribute("search_type","newsfeed");
			response.sendRedirect("search.jsp");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
		try{
			String search_text=request.getParameter("search");
			ResultSet rs;
			String searchresults="";
			if(session.getAttribute("search_type").equals("students")){
			rs=st.executeQuery("Select course_id from student natural join follow " +
					"where user_id='"+search_text+"' or name='"+
					search_text+"' or email='"+search_text+"' or rollno='"+search_text+"' order by name;");
			searchresults+= "<div style=\"float:left; margin-left:100px\"><p> <b>Courses followed by:" +
					search_text + "are </b><br>";
			while(rs.next()){
				searchresults+="<br>" + rs.getString(1);
			}
			searchresults+="</div>";
			}
			else if(session.getAttribute("search_type").equals("newsfeed")){//search via user_id, course_id or tag
			rs=st.executeQuery("Select user_id,news_text,time_stamp" +
					" from newsfeed natural join newstag where user_id='"+search_text+"' or course_id='"+
					search_text+"' or tag='"+search_text+"' order by time_stamp;");
			searchresults += "<div style=\"float:left; margin-left:100px\"><p> <b>News Feed: </b><br>";
			while(rs.next()){
				searchresults+= "<br>"+rs.getString(1) + " &nbsp; &nbsp; " + rs.getString(2) + "&nbsp; &nbsp; " +
				rs.getString(3);
			}
			searchresults+="</div>";
			}
			session.setAttribute("searchresults",searchresults);
			response.sendRedirect("searchresults.jsp");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
