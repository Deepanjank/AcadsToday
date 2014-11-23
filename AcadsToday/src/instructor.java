

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
 * Servlet implementation class instructor
 */
public class instructor extends HttpServlet {
	private static final long serialVersionUID = 1L; 
	public static Connection conn1 =null;
	Statement st=null;
	int instructor_review=1;
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
	public instructor() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String bool_instructor=null;
		String retval="";
		try{
			bool_instructor=request.getParameter("select_dept").toString();
			if(bool_instructor.equals("instructor")){
				String department=request.getParameter("Departments");
				ResultSet rs;
				rs=st.executeQuery("Select name from department natural join instructor where dept_name='"+department+"';");
				retval="<form action=\"instructor\" method=\"get\" style=\"float:left;margin-left:100px\"> <div style=\"width:1200px\"><div style=\"float:left; margin-left:200px;width:200px\">Select the instructor:</div><div style=\"float:left;margin-left:40px;width:300px\"><select name=\"instructors\">\n";
				while(rs.next()){
					System.out.println("Comeon");
					retval+="<option value=\""+rs.getString(1)+"\">"+rs.getString(1)+"</option>\n";
				}
				retval+="</select></div><div style=\"margin-left:300px\"><input type=\"submit\" value=\"Select\"><input type=\"hidden\" name=\"select_dept\" value=\"instructor_page\"></div></div></form>";
				session.setAttribute("instructors",retval);
				//System.out.println(retval);
				response.sendRedirect("instructor.jsp");
			}
			else if(bool_instructor.equals("instructor_page") || bool_instructor.equals("seeMoreReview")){
				String instructor_name=(String)request.getParameter("instructors");
				if(bool_instructor.equals("seeMoreReview")){
					instructor_review+=10;
					instructor_name=(String)session.getAttribute("instructor_name");
				}
				session.setAttribute("instructor_name",instructor_name);
				ResultSet rs;
				rs=st.executeQuery("Select instructor_id from instructor where name='"+instructor_name+"';");
				rs.next();
				String instructor_id=rs.getString(1);
				session.setAttribute("instructor_id",instructor_id);
				rs=st.executeQuery("Select user_id,review_text from instructorreview where instructor_id='"+
						instructor_id.toString()+"' order by upvotes limit "+instructor_review+";");
				int i=0;
				String review_text="";
				while(rs.next()){
					review_text+="<br><br><br><br><br><br><div style=\"float:left;margin-left:125px; width: " +
							"1000px;padding: 25px;text-align: left;font-size: 100%;color:" +
							"black;border: 1px solid navy;background-color:" +
							"#f1ffff\"><span style=\"font-size:80%\"><i>by "+rs.getString(1)+"</i></span><p>"+rs.getString(2)+"</p></div><br><br><br>";
					i++;
				}
				String query = "Select rating from instructorrating where instructor_id='"+instructor_id+
				"' and user_id='"+session.getAttribute("Username")+"';";
				System.out.println(query);
				
				rs=st.executeQuery(query);
				
				session.setAttribute("rating", -1);
				session.setAttribute("firststar", "star1.jpg");
				session.setAttribute("secondstar", "star1.jpg");
				session.setAttribute("thirdstar", "star1.jpg");
				session.setAttribute("fourthstar", "star1.jpg");
				session.setAttribute("fifthstar", "star1.jpg");
				while(rs.next()){
					session.setAttribute("rating",rs.getString(1));
					if(rs.getInt(1)>=1) session.setAttribute("firststar", "star.jpg");
					if(rs.getInt(1)>=2) session.setAttribute("secondstar", "star.jpg");
					if(rs.getInt(1)>=3) session.setAttribute("thirdstar", "star.jpg");
					if(rs.getInt(1)>=4) session.setAttribute("fourthstar", "star.jpg");
					if(rs.getInt(1)>=5) session.setAttribute("fifthstar", "star.jpg");
					
					System.out.println("Now we have a rating "+rs.getString(1));
					break;
				}
				session.setAttribute("instructor_review_code", review_text);
				response.sendRedirect("instructorreview.jsp");
			}
			else if(bool_instructor.equals("rating")){
				System.out.println("Reached here");
				String val =request.getParameter("val")+"";
				String rating = session.getAttribute("rating")+"";
				String instructor = (String) session.getAttribute("instructor_id");
				String user = (String) session.getAttribute("Username");
				System.out.println("here is "+rating);
				if(rating.equals("-1")){
					System.out.println("inserting");
					st.executeUpdate("insert into instructorrating values('"+user+"','"+instructor+"','"+val+"');");
					session.setAttribute("rating",val);
					if(val.charAt(0)>='1') session.setAttribute("firststar", "star.jpg");
					if(val.charAt(0)>='2') session.setAttribute("secondstar", "star.jpg");
					if(val.charAt(0)>='3') session.setAttribute("thirdstar", "star.jpg");
					if(val.charAt(0)>='4') session.setAttribute("fourthstar", "star.jpg");
					if(val.charAt(0)>='5') session.setAttribute("fifthstar", "star.jpg");
				}
				else{
					System.out.println("updating");
					String str = "update instructorrating set rating = "+val+" where user_id = '"+user+"' and instructor_id = '"+instructor+"'";
					System.out.println(str);
					st.executeUpdate(str);
					
					session.setAttribute("firststar", "star1.jpg");
					session.setAttribute("secondstar", "star1.jpg");
					session.setAttribute("thirdstar", "star1.jpg");
					session.setAttribute("fourthstar", "star1.jpg");
					session.setAttribute("fifthstar", "star1.jpg");
					
					if(val.charAt(0)>='1') session.setAttribute("firststar", "star.jpg");
					if(val.charAt(0)>='2') session.setAttribute("secondstar", "star.jpg");
					if(val.charAt(0)>='3') session.setAttribute("thirdstar", "star.jpg");
					if(val.charAt(0)>='4') session.setAttribute("fourthstar", "star.jpg");
					if(val.charAt(0)>='5') session.setAttribute("fifthstar", "star.jpg");
					session.setAttribute("rating",val);
				}
				System.out.println("Finally");
				response.sendRedirect("instructorreview.jsp");
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
				session.setAttribute("instructors","");
				response.sendRedirect("instructor.jsp");
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
