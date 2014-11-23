

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 * Servlet implementation class Add
 */
@MultipartConfig(maxFileSize = 16177215)
public class Add extends HttpServlet {
	private static final long serialVersionUID = 1L; 
	public static Connection conn1 =null;
	Statement st=null;
	int news_id;
	int course_review_id;
	int instructor_review_id;
	int material_id;
	public void init() throws ServletException {
		String dbURL2 = "jdbc:postgresql://localhost/cs387";
		String user = "deepanjan";
		String pass = "najnapeed";

		System.out.println("1");
		try {
			Class.forName("org.postgresql.Driver");
			conn1 = DriverManager.getConnection(dbURL2, user, pass);
			st = conn1.createStatement();
			ResultSet rs;
			rs=st.executeQuery("Select count(*) from newsfeed;");
			rs.next();
			news_id=rs.getInt(1);
			rs=st.executeQuery("Select count(*) from coursereview;");
			rs.next();
			course_review_id=rs.getInt(1);
			rs=st.executeQuery("Select count(*) from instructorreview;");
			rs.next();
			instructor_review_id=rs.getInt(1);
			rs=st.executeQuery("Select count(*) from material;");
			rs.next();
			material_id=rs.getInt(1);
			System.out.println("init"+conn1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Add() {
		super();
		System.out.println("2");
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type=(String) request.getParameter("type");
		HttpSession session = request.getSession();
		if(type.equals("course")){
			String method=(String)request.getParameter("add");
			if(method.equals("news")){
				try{
					news_id++;
					String news_text=(String)request.getParameter("News");
					String tags=(String)request.getParameter("Tags");
					st.executeUpdate("Insert into newsfeed values('"+news_id+"','"
							+session.getAttribute("Username").toString()+"','"+
							session.getAttribute("course_id").toString()
							+"','"+news_text+"');");
					String[] temp=tags.split(" ");
					for(int i=0;i<temp.length;i++)
					{
						System.out.println(temp[i]);
						st.executeUpdate("Insert into newstag values('"+news_id+"','"+temp[i]+"');");
					}
					response.sendRedirect("coursereview.jsp");
				}
				catch(Exception e){
					news_id--;
					e.printStackTrace();
				}
			}
			else if(method.equals("review")){
				try{
				course_review_id++;
				String review_text=(String)request.getParameter("Review");
				st.executeUpdate("Insert into coursereview values('"+course_review_id+"','"
						+session.getAttribute("course_id").toString()+"','"+
						session.getAttribute("Username").toString()+"','"+review_text+"',0,0);");
				String course_name=session.getAttribute("course_id").toString();
				ResultSet rs;
				rs=st.executeQuery("Select user_id,review_text from coursereview where course_id='"+
						session.getAttribute("course_id").toString()+"' order by upvotes limit 10;");
				int i=0;
				review_text="";
				while(rs.next()){
					review_text+="<br><br><br><br><br><br><div style=\"float:left;margin-left:125px; width: " +
					"1000px;padding: 25px;text-align: left;font-size: 100%;color:" +
					"black;border: 1px solid navy;background-color:" +
					"#f1ffff\">"+"<span style=\"font-size: 80%\"><i>by "+rs.getString(1)+
					"</i></span><p>"+rs.getString(2)+"</p></div><br><br><br>";
					i++;
				}
				session.setAttribute("course_review_code", review_text);
				response.sendRedirect("coursereview.jsp");
				}
				catch(Exception e){
					course_review_id--;
					e.printStackTrace();
				}
			}
			else if(method.equals("material")){
				try{
					material_id++;
					System.out.println(material_id);
					InputStream inputStream = null;
					Part filePart = request.getPart("uploadField");
					String fileName = request.getParameter("dispName");
					String filedesc = request.getParameter("description");
					String fileName2 = request.getParameter("uploadField");
					System.out.println(fileName2);
			        if (filePart != null) {
			        	System.out.print("deepanjan");
			            // prints out some information for debugging
			            System.out.println(filePart.getName());
			            System.out.println(filePart.getSize());
			            System.out.println(filePart.getContentType());
			            inputStream = filePart.getInputStream();
			        }
			     // constructs SQL statement
			        String sql = "INSERT INTO material (material_id, course_id, user_id, materialname, description, material, rating) values (?, ?, ?,?,?,?,?)";
			        PreparedStatement statement = conn1.prepareStatement(sql);
		            statement.setString(1, ""+material_id);
		            statement.setString(2, session.getAttribute("course_id").toString());
		            statement.setString(3, session.getAttribute("Username").toString());
		            statement.setString(4, fileName);
		            statement.setString(5, filedesc);
		            if (inputStream != null){
		                statement.setBinaryStream(6, inputStream,(int)filePart.getSize());
		            }
		            statement.setInt(7,0);
		            statement.executeUpdate();
		            response.sendRedirect("./coursereview.jsp");
				}
				catch(Exception e){
					material_id--;
					e.printStackTrace();
				}
			}
		}
		else if(type.equals("instructor")){
			try{
				
				instructor_review_id++;
				System.out.println(instructor_review_id);
				String instructor_name=(String)request.getParameter("instructors");
				String review_text=(String)request.getParameter("Review");
				st.executeUpdate("Insert into instructorreview values('"+instructor_review_id+"','"
						+session.getAttribute("instructor_id").toString()+"','"+
						session.getAttribute("Username").toString()+"','"+review_text+"',0,0);");
				ResultSet rs;
				String instructor_id=session.getAttribute("instructor_id").toString();
				rs=st.executeQuery("Select user_id,review_text from instructorreview where instructor_id='"+
						instructor_id.toString()+"' order by upvotes limit 10;");
				int i=0;
				review_text="";
					while(rs.next()){
						review_text+="<br><br><br><br><br><br><div style=\"float:left;margin-left:125px; width: " +
								"1000px;padding: 25px;text-align: left;font-size: 100%;color:" +
								"black;border: 1px solid navy;background-color:" +
								"#f1ffff\"><span style=\"font-size:80%\"><i>by "+rs.getString(1)+"</i></span><p>"+rs.getString(2)+"</p></div><br><br><br>";
					i++;
				}
				session.setAttribute("instructor_review_code", review_text);
				response.sendRedirect("instructorreview.jsp");
			}
			catch(Exception e){
				instructor_review_id--;
				e.printStackTrace();
			}
		}
	}
}
