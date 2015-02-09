

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
	public static Statement st2=null;
	public void init() throws ServletException {
		String dbURL2 = "jdbc:postgresql://localhost/cs387";
		String user = "deepanjan";
		String pass = "najnapeed";

		try {
			Class.forName("org.postgresql.Driver");
			conn1 = DriverManager.getConnection(dbURL2, user, pass);
			st = conn1.createStatement();
			st2 = conn1.createStatement();
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
			String aid="<div style=\"float:left;margin-left:500px\"><b>Enter the user id:<b></div>";
			session.setAttribute("search_aid",aid);
			response.sendRedirect("search.jsp");
		}
		else if(type.equals("newsfeed")){
			session.setAttribute("search_type","newsfeed");
			String aid="<div style=\"float:left;margin-left:400px\">" +
			"<b> Enter the user-id, course-id or newstag:<b></div>";
			session.setAttribute("search_aid",aid);
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
			String searchresults="<div style=\"float:left;margin-left:500px\"><br><b>INFO OF THE CLOSEST MATCH:</b></div>";
			if(session.getAttribute("search_type").equals("students")){

				rs=st.executeQuery("Select course_id,title from student natural join follow natural join course " +
						"where user_id='"+search_text+"' or name='"+
						search_text+"' or email='"+search_text+"' or rollno='"+search_text+"' order by name;");
				searchresults+= "<div style=\"float:left;margin-left:500px\"><br><b>Courses followed:</b></div><br>" +
				"<table border=\"1\" style=\"margin-left:125px\" width=\"82%\"><col style=\"width:40%\">" +
				"<col style=\"width:60%\">" +
				"<thead><tr bgcolor=\"#9999CC\"><th>Course-id</th><th>Course Name</th>" +
				"</tr></thead><tbody bgcolor=\"#66FFCC\">";
				while(rs.next()){
					searchresults+="<tr><td>"+rs.getString(1)+"</td><td>"+rs.getString(2)+"</td></tr>";
				}
				searchresults+="</table><br><br><div style=\"float:left;margin-left:500px\"><b>Related Reviews:<b></div>";
				//session.setAttribute("searchresults", searchresults);

				rs=st.executeQuery("Select user_id,review_text,date_stamp,time_stamp from coursereview natural join student where user_id='"+
						search_text+"' or name='"+
						search_text+"' or email='"+search_text+"' or rollno='"+search_text+"' order by upvotes-downvotes desc limit 5;");
				int i=0;
				searchresults+="<div style=\"float:top;\">";
				//System.out.println("dfghj "+count);
				while(rs.next()){
					searchresults+="<div style=\"float:top;margin-top:30px\"><div style=\"float:left;margin-left:125px; width: " +
					"1000px;padding: 25px;text-align: left;font-size: 100%;color:" +
					"black;border: 1px solid navy;background-color:" +
					"#f1ffff\"><div style = \"float:left;width:800px\">"+"<span style=\"font-size: 80%\"><i>by <b>"+rs.getString(1)+
					"</b></i></span><p>"+rs.getString(2)+"</p></div><div style = \"float:left;margin-left:20px\">"+
					"<div style = \"float:top;margin-top:10px\">Date:"+rs.getString(3)+"</div>"+
					"<div style = \"float:top;margin-top:20px\">Time:"+rs.getString(4)+"</div>"+"</div></div></div>";
				}
				searchresults+="</div><br><br><div style=\"float:left;margin-left:500px\"><b>Related Instructor Reviews:</b></div>"+
				"<div style=\"float:top;\">";
				rs=st.executeQuery("Select user_id,review_text,date_stamp,time_stamp from instructorreview natural join student where user_id='"+
						search_text+"' or name='"+
						search_text+"' or email='"+search_text+"' or rollno='"+search_text+"' order by upvotes-downvotes desc limit 5;");
				while(rs.next()){
					searchresults+="<div style=\"float:top;margin-top:30px\"><div style=\"float:left;margin-left:125px; width: " +
					"1000px;padding: 25px;text-align: left;font-size: 100%;color:" +
					"black;border: 1px solid navy;background-color:" +
					"#f1ffff\"><div style = \"float:left;width:800px\">"+"<span style=\"font-size: 80%\"><i>by <b>"+rs.getString(1)+
					"</b></i></span><p>"+rs.getString(2)+"</p></div><div style = \"float:left;margin-left:20px\">"+
					"<div style = \"float:top;margin-top:10px\">Date:"+rs.getString(3)+"</div>"+
					"<div style = \"float:top;margin-top:20px\">Time:"+rs.getString(4)+"</div>"+"</div></div></div>";
				}

				searchresults+="</div><br><br><div style=\"float:left;margin-left:500px\"><b> Related Material Uploads:</b></div>"+
				"<div style=\"float:top;\">";
				String query = "Select materialname,description,user_id,date_stamp,time_stamp from material natural join student where user_id='"+search_text+"' or name='"+
				search_text+"' or email='"+search_text+"' or rollno='"+search_text+
				"' order by date_stamp desc, time_stamp desc limit 5;";	
				rs=st.executeQuery(query);
				while(rs.next()){
					searchresults+="<div style=\"float:top;margin-top:30px\"><div style=\"float:left;margin-left:125px; width: " +
					"1000px;padding: 25px;text-align: left;font-size: 100%;color:" +
					"black;border: 1px solid navy;background-color:" +
					"#f1ffff\">"+"<div style=\"float:left;width:800px\"><form method=\"get\" action=\"course\"><input type=\"hidden\" name=\"user\" value=\""+rs.getString(3)+"\"> <input type=\"hidden\" name=\"select_dept\" value=\"retrieveMaterial\">  <input type=\"submit\" name=\"fileName\" style=\"background-color:#6495ed;\" value=\""+rs.getString(1)+"\">" 
					+"<span style=\"font-size:80%\"><i> by <b>"+rs.getString(3)+"</b></i></span><p>"+rs.getString(2)+
					"</p></form></div><div style=\"float:left;margin-left:10px;\">" +
					"<div style = \"float:top;margin-top:10px\">Date:"+rs.getString(4)+"</div>"+
					"<div style = \"float:top;margin-top:20px\">Time:"+rs.getString(5)+"</div>"+
					"</div></div></div><br><br><br>";
					i++;
				};
				searchresults+="</div>";
				session.setAttribute("searchresults", searchresults);
			}

			else if(session.getAttribute("search_type").equals("newsfeed")){//search via user_id, course_id or tag
				rs=st.executeQuery("Select distinct course_id,user_id,news_text,date_stamp,time_stamp" +
						" from newsfeed natural join newstag where user_id='"+search_text+"' or course_id='"+
						search_text+"' or tag='"+search_text+"' order by date_stamp desc ,time_stamp desc limit 10;");
				searchresults = "	<div style=\"float:left\"><br><b>News feed:</b></div><br>" +
				"<table border=\"1\" width=\"100%\"><col style=\"width:20%\">" +"<col style=\"width:20%\">"+
				"<col style=\"width:30%\"><col style=\"width:15%\"><col style=\"width=\"15%\"" +
				"<thead><tr bgcolor=\"#9999CC\"><th>Course-id</th><th>User</th><th>News</th><th>Date</th><th>Time</th>" +
				"</tr></thead><tbody bgcolor=\"#66FFCC\">";

				while(rs.next()){
					searchresults+= "<tr><td>"+rs.getString(1)+"</td><td>"+rs.getString(2)+"</td><td>" +
					rs.getString(3) + "</td><td>" +
					rs.getString(4) + "</td><td>" +
					rs.getString(5) + "</td></tr>";
				}
				searchresults+="</tbody></table>";
			}
			session.setAttribute("searchresults",searchresults);
			response.sendRedirect("searchresults.jsp");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}