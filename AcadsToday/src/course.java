

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class course
 */
public class course extends HttpServlet {
	private static final int BUFFER_SIZE = 4096; 
	private static final long serialVersionUID = 1L; 
	public static Connection conn1 =null;
	Statement st=null;
	int material_count=1;
	int review_count=1;
	public void init() throws ServletException {
		String dbURL2 = "jdbc:postgresql://localhost/cs387";
		String user = "deepanjan";
		String pass = "najnapeed";
		try {
			System.out.println("vikasgarg");
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
			else if(bool_course.equals("course_page") || bool_course.equals("seeMoreReview")){
				String course_name=(String)request.getParameter("Courses");
				if(bool_course.equals("seeMoreReview")){review_count+=10;}
				ResultSet rs;
				if(course_name.equals("deepanjan")){
					course_name=session.getAttribute("course_name").toString();
				}
				int count =0;
				for(int i = 0; i < course_name.length(); i++){
					if(Character.isWhitespace(course_name.charAt(i))){
						count++;
						if(count==2){
							count = i;
							break;
						}
					}
				}
				System.out.println(count);
				String course_id = course_name.toString().substring(0,count);
				rs=st.executeQuery("Select user_id,review_text from coursereview where course_id='"+
						course_id+"' order by upvotes limit "+review_count+";");
				String query = "Select * from follow where course_id='"+course_id+
				"' and user_id='"+session.getAttribute("Username")+"';";
				System.out.println(query);

				int i=0;
				String review_text="";
				System.out.println("dfghj "+count);
				while(rs.next()){
					review_text+="<br><br><br><br><br><br><div style=\"float:left;margin-left:125px; width: " +
					"1000px;padding: 25px;text-align: left;font-size: 100%;color:" +
					"black;border: 1px solid navy;background-color:" +
					"#f1ffff\">"+"<span style=\"font-size: 80%\"><i>by "+rs.getString(1)+
					"</i></span><p>"+rs.getString(2)+"</p></div><br><br><br>";
					i++;
				}
				System.out.println("aaaaa");
				rs=st.executeQuery(query);
				if (!rs.next()){
					session.setAttribute("follow", "Follow");
					System.out.println("we don't have a follow");
				}
				else{
					session.setAttribute("follow","Following");
					System.out.println("Now we have a follow");
				}
				String query2 = "Select rating from courserating where course_id='"+course_id+
				"' and user_id='"+session.getAttribute("Username")+"';";
				System.out.println("query2 is "+query2);
				rs=st.executeQuery(query2);
				
				session.setAttribute("rating", -1);
				session.setAttribute("firststar_course", "star1.jpg");
				session.setAttribute("secondstar_course", "star1.jpg");
				session.setAttribute("thirdstar_course", "star1.jpg");
				session.setAttribute("fourthstar_course", "star1.jpg");
				session.setAttribute("fifthstar_course", "star1.jpg");
				while(rs.next()){
					session.setAttribute("rating",rs.getString(1));
					if(rs.getInt(1)>=1) session.setAttribute("firststar_course", "star.jpg");
					if(rs.getInt(1)>=2) session.setAttribute("secondstar_course", "star.jpg");
					if(rs.getInt(1)>=3) session.setAttribute("thirdstar_course", "star.jpg");
					if(rs.getInt(1)>=4) session.setAttribute("fourthstar_course", "star.jpg");
					if(rs.getInt(1)>=5) session.setAttribute("fifthstar_course", "star.jpg");
					
					//System.out.println("Now we have a follow");
					break;
				}
				System.out.println("here "+session.getAttribute("rating"));
				session.setAttribute("course_review_code", review_text);
				session.setAttribute("course_name",course_name);
				session.setAttribute("course_id",course_id);
				session.setAttribute("jsp", "course_review");
				response.sendRedirect("coursereview.jsp");
			}
			else if(bool_course.equals("follow")){
				System.out.println("Reached here");
				String follow = (String) session.getAttribute("follow");
				String course = (String) session.getAttribute("course_id");
				String user = (String) session.getAttribute("Username");
				String jsp = (String) session.getAttribute("jsp");
				System.out.println("here is "+course);
				if(follow == "Follow"){
					System.out.println("changing");
					st.executeUpdate("insert into follow values('"+user+"','"+course+"');");
					session.setAttribute("follow","Following");
				}
				else{
					System.out.println("again changing");
					String str = "delete from follow where user_id = '"+user+"' and course_id = '"+course+"';";
					System.out.println(str);
					st.executeUpdate(str);

					session.setAttribute("follow","Follow");
				}
				System.out.println("Finally");
				response.sendRedirect("coursereview.jsp");
			}
			else if(bool_course.equals("material") || bool_course.equals("seeMoreMaterial")){
				if(bool_course.equals("seeMoreMaterial")){
					material_count+=10;
				}
				ResultSet rs;
				String course_id=session.getAttribute("course_id").toString();
				String query = "Select materialname,description,user_id from material where course_id='"+course_id+
				"' order by rating limit "+material_count+";";			
				int i=0;
				rs=st.executeQuery(query);
				String material_text="";
				while(rs.next()){
					material_text+="<br><br><br><br><br><br><div style=\"float:left;margin-left:125px; width: " +
					"1000px;padding: 25px;text-align: left;font-size: 100%;color:" +
					"black;border: 1px solid navy;background-color:" +
					"#f1ffff\">"+"<form method=\"get\" action=\"course\"><input type=\"hidden\" name=\"user\" value=\""+rs.getString(3)+"\"> <input type=\"hidden\" name=\"select_dept\" value=\"retrieveMaterial\">  <input type=\"submit\" name=\"fileName\" value=\""+rs.getString(1)+"\">" 
					+"<span style=\"font-size:80%\"><i>by "+rs.getString(3)+"</i></span><p>"+rs.getString(2)+"</p></form></div><br><br><br>";
					i++;
				}
				System.out.println("aaaaa");
				session.setAttribute("course_material_code", material_text);
				session.setAttribute("course_id",course_id);
				session.setAttribute("jsp", "course_review");
				response.sendRedirect("coursematerial.jsp");

			}
			else if(bool_course.equals("retrieveMaterial"))
			{
				ResultSet rs;
				String course_id=session.getAttribute("course_id").toString();
				String fileName=request.getParameter("fileName");
				String user_id=request.getParameter("user");
				String query = "Select material,materialname from material where course_id='"+course_id+
				"' and user_id='"+session.getAttribute("Username")+"' order by rating limit 10;";			
				PreparedStatement ps = conn1.prepareStatement("SELECT material FROM material WHERE materialname = ? and user_id=?");
				ps.setString(1, fileName);
				ps.setString(2, user_id);
				rs = ps.executeQuery();
				if(rs.next()) {
					InputStream inputStream = rs.getBinaryStream("material");
					int fileLength = inputStream.available();// use the data in some way here

					System.out.println("fileLength = " + fileLength);

					ServletContext context = getServletContext();

					// sets MIME type for the file download
					String mimeType = context.getMimeType(fileName);
					if (mimeType == null) {        
						mimeType = "application/octet-stream";
					}
					response.setContentType(mimeType);
					response.setContentLength(fileLength);
					String headerKey = "Content-Disposition";
					String headerValue = String.format("attachment; filename=\"%s\"", fileName);
					response.setHeader(headerKey, headerValue);

					// writes the file to the client
					OutputStream outStream = response.getOutputStream();

					byte[] buffer = new byte[BUFFER_SIZE];
					int bytesRead = -1;

					while ((bytesRead = inputStream.read(buffer)) != -1) {
						outStream.write(buffer, 0, bytesRead);
					}

					inputStream.close();
					outStream.close();
				}
				else {
					// no file found
					response.getWriter().print("File not found ");  
				}
				rs.close();
				ps.close();
				response.sendRedirect("coursematerial.jsp");
				
			}
			else if(bool_course.equals("rating")){
				System.out.println("Reached here");
				String val =request.getParameter("val")+"";
				String rating = session.getAttribute("rating")+"";
				String course = (String) session.getAttribute("course_id");
				String user = (String) session.getAttribute("Username");
				System.out.println("here is "+rating);
				if(rating.equals("-1")){
					System.out.println("inserting");
					st.executeUpdate("insert into courserating values('"+user+"','"+course+"','"+val+"');");
					session.setAttribute("rating",val);
					if(val.charAt(0)>='1') session.setAttribute("firststar_course", "star.jpg");
					if(val.charAt(0)>='2') session.setAttribute("secondstar_course", "star.jpg");
					if(val.charAt(0)>='3') session.setAttribute("thirdstar_course", "star.jpg");
					if(val.charAt(0)>='4') session.setAttribute("fourthstar_course", "star.jpg");
					if(val.charAt(0)>='5') session.setAttribute("fifthstar_course", "star.jpg");
				}
				else{
					System.out.println("updating");
					String str = "update courserating set rating = "+val+" where user_id = '"+user+"' and course_id = '"+course+"'";
					System.out.println(str);
					st.executeUpdate(str);
					
					session.setAttribute("firststar_course", "star1.jpg");
					session.setAttribute("secondstar_course", "star1.jpg");
					session.setAttribute("thirdstar_course", "star1.jpg");
					session.setAttribute("fourthstar_course", "star1.jpg");
					session.setAttribute("fifthstar_course", "star1.jpg");
					
					if(val.charAt(0)>='1') session.setAttribute("firststar_course", "star.jpg");
					if(val.charAt(0)>='2') session.setAttribute("secondstar_course", "star.jpg");
					if(val.charAt(0)>='3') session.setAttribute("thirdstar_course", "star.jpg");
					if(val.charAt(0)>='4') session.setAttribute("fourthstar_course", "star.jpg");
					if(val.charAt(0)>='5') session.setAttribute("fifthstar_course", "star.jpg");
					session.setAttribute("rating",val);
				}
				System.out.println("Finally");
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

	}

}
