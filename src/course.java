

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	Statement st=null,st1=null;
	int material_count=1;
	int review_count=1;
	int comment_id=1;
	public void init() throws ServletException {
		String dbURL2 = "jdbc:postgresql://localhost/cs387";
		String user = "deepanjan";
		String pass = "najnapeed";
		try {
			System.out.println("vikasgarg");
			Class.forName("org.postgresql.Driver");
			conn1 = DriverManager.getConnection(dbURL2, user, pass);
			st = conn1.createStatement();
			st1 = conn1.createStatement();
			ResultSet rs;
			rs = st.executeQuery("Select count(*) from coursecomments;");
			rs.next();
			comment_id=rs.getInt(1);
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
	protected void addCourseReview(HttpServletRequest request, HttpServletResponse response, String course_name) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String bool_course=null;
		String retval="";
		try{

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
			rs=st.executeQuery("Select user_id,review_text,review_id from coursereview where course_id='"+
					course_id+"' order by upvotes-downvotes desc limit "+review_count+";");
			String query = "Select * from follow where course_id='"+course_id+
			"' and user_id='"+session.getAttribute("Username")+"';";
			System.out.println(query);

			int i=0;
			String review_text="<div style=\"float:top;\">";
			System.out.println("dfghj "+count);
			while(rs.next()){
				String reviewid = rs.getString(3);
				ResultSet newrs;
				String upquery = "Select UporDown from coursevotes where user_id='"+session.getAttribute("Username")+"' and review_id='"+reviewid+"';";
				System.out.println("upquery is "+upquery);
				newrs=st1.executeQuery(upquery);
				String up = "Upvote";
				String down = "Downvote";
				while(newrs.next()){
					System.out.println("reached reached "+newrs.getBoolean(1));
					if(newrs.getBoolean(1)) up = "Upvoted";
					else down="Downvoted";
				}
				upquery = "Select upvotes,downvotes from coursereview where review_id='"+reviewid+"';";
				newrs=st1.executeQuery(upquery);
				newrs.next();
				int upvote = newrs.getInt(1);
				int downvote = newrs.getInt(2);
				System.out.println(upvote);
				System.out.println(downvote);
				String review_comment="<div style=\"float:left;margin-left:125px;color:black;padding: 10px;" +
				"text-align: left;width:1030px;border: 1px solid navy;background-color:#f1ffff \">Recent Comments</div>";
				String comment_query="Select comment_text,user_id from coursecomments where review_id='"+
				reviewid+"' order by date_stamp desc,time_stamp desc;";
				newrs=st1.executeQuery(comment_query);
				while(newrs.next()){
					review_comment+="<div style=\"float:left;margin-left:125px;color:black;padding: 10px;" +
					"text-align: left;width:1030px;border: 1px solid navy;background-color:#f1ffff \">" +
					"<span style=\"font-size: 80%\"><i>by </i><b>"+newrs.getString(2);
					review_comment+="</span></b><br><span style=\"font-size: 80%\">";
					review_comment+=newrs.getString(1);
					review_comment+="</span></div>";
				}
				review_text+="<div style=\"float:top;margin-top:30px\"><div style=\"float:left;margin-left:125px; width: " +
				"1000px;padding: 25px;text-align: left;font-size: 100%;color:" +
				"black;border: 1px solid navy;background-color:" +
				"#f1ffff\"><div style = \"float:left;width:800px\">"+"<span style=\"font-size: 80%\"><i>by <b>"+rs.getString(1)+
				"</b></i></span><p>"+rs.getString(2)+"</p></div><div style = \"float:left;margin-left:20px\"><div "+
				"style = \"float:top;margin-left:10px\">"+
				"<form action=\"course\" method=\"get\"><input type=\"submit\"  value=\""+up+"\" style"+
				"=\"background-color:#6495ed; \">:"+upvote+"<input type=\"hidden\" name=\"reviewid\" value="+rs.getString(3)+">"+
				" <input type=\"hidden\" name=\"select_dept\" value=\"upvote\"></form> </div>"+
				"<div style = \"float:top;margin-top:20px\">"+
				"<form action=\"course\" method=\"get\"><input type=\"submit\"  value=\""+down+"\" style"+
				"=\"background-color:#6495ed; \">:"+downvote+"<input type=\"hidden\" name=\"reviewid\" value="+reviewid+">"+
				" <input type=\"hidden\" name=\"select_dept\" value=\"downvote\"></form> </div></div></div><br><br><br>\n"
				+"</div>"+review_comment+"<div style=\"float:top;margin-top:10px;width:1000px;margin-left:125px;padding:25px;border:1px solid navy;background-color:#ccffff\">" +
				"<form action=\"course\" method=\"post\" style=\"margin-left:100px\">Enter your comment:<br><textarea style=\"width:800px\" type=\"text\" name=\"Comment\"></textarea><br>" +
				"<input type=\"hidden\" name=\"id\" value=\""+reviewid+"\"><input type=\"submit\"style"+
				"=\"background-color:#6495ed;\" name=\"CourseComment\" value=\"Submit Comment\"></form></div></div>\t";
				i++;
			}
			review_text+="</div>";
			//System.out.println("aaaaa");
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
			//System.out.println("query2 is "+query2);
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

			query2 = "Select rating from course where course_id='"+course_id+"';";
			//System.out.println("query2 is "+query2);
			rs=st.executeQuery(query2);
			rs.next();
			//System.out.println("here "+session.getAttribute("rating"));
			session.setAttribute("course_review_code", review_text);
			session.setAttribute("course_name",course_name);
			session.setAttribute("course_id",course_id);
			session.setAttribute("jsp", "course_review");
			session.setAttribute("course_rating", rs.getString(1));
			response.sendRedirect("coursereview.jsp");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
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
				retval+="</select></div><div style=\"margin-left:300px\"><input type=\"submit\" style=\"background-color:#6495ed;\" value=\"Select\"><input type=\"hidden\" name=\"select_dept\" value=\"course_page\"></div></div></form>";
				session.setAttribute("courses",retval);
				//System.out.println(retval);
				response.sendRedirect("course.jsp");
			}
			else if(bool_course.equals("course_page") || bool_course.equals("seeMoreReview")){
				if(bool_course.equals("seeMoreReview")){review_count+=10;}
				addCourseReview(request, response,request.getParameter("Courses"));
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
				"' order by date_stamp desc, time_stamp desc limit "+material_count+";";			
				int i=0;
				rs=st.executeQuery(query);
				String material_text="<div style=\"float:top;\">";
				while(rs.next()){
					material_text+="<div style=\"float:top;margin-top:30px\"><div style=\"float:left;margin-left:125px; width: " +
					"1000px;padding: 25px;text-align: left;font-size: 100%;color:" +
					"black;border: 1px solid navy;background-color:" +
					"#f1ffff\">"+"<form method=\"get\" action=\"course\"><input type=\"hidden\" name=\"user\" value=\""+rs.getString(3)+"\"> <input type=\"hidden\" name=\"select_dept\" value=\"retrieveMaterial\">  <input type=\"submit\" name=\"fileName\" style=\"background-color:#6495ed;\" value=\""+rs.getString(1)+"\">" 
					+"<span style=\"font-size:80%\"><i> by <b>"+rs.getString(3)+"</b></i></span><p>"+rs.getString(2)+
					"</p></form></div></div><br><br><br>";
					i++;
				}
				material_text+="</div>";
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
				"' and user_id='"+session.getAttribute("Username")+"' order by date_stamp desc,time_stamp desc limit 10;";			
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
				//response.sendRedirect("coursematerial.jsp");

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
				ResultSet rs;
				String str = "select sum(rating),count(rating) from courserating where course_id='"+course+"';";
				System.out.println(str);
				rs = st.executeQuery(str);
				rs.next();
				double rat = 0;
				double firnum = rs.getInt(1);
				double secondnum = rs.getInt(2);
				if(rs.getInt(2)>0) rat = firnum/secondnum;
				DecimalFormat df = new DecimalFormat("#.##");
				str = "update course set rating = "+df.format(rat)+" where course_id='"+course+"';";
				System.out.println(str);
				st.executeUpdate(str);

				session.setAttribute("course_rating",df.format(rat)+"");

				System.out.println("Finally");


				response.sendRedirect("coursereview.jsp");
			}
			else if(bool_course.equals("upvote")){
				System.out.println("Reached in upvotes");
				String text = session.getAttribute("course_review_code").toString();
				String[] all = text.split("\n");
				//System.out.println(all[0]);
				String sear = request.getParameter("reviewid")+"";

				sear = "name=\"reviewid\" value="+sear+">";
				String ans="";
				for(int i=0;i<all.length;i++){
					if(all[i].contains(sear)){
						//System.out.println("which is "+i);
						if(all[i].contains("Upvoted")){
							all[i]=all[i].replaceAll("Upvoted", "Upvote");

							String checking = "value=\"Upvote\" style=\"background-color:#6495ed; \">:";
							int position = all[i].indexOf(checking);
							position = position+checking.length();
							int position2 = position;
							System.out.println("char is "+all[i].charAt(position));
							int num=0;
							while(true){
								if(all[i].charAt(position2)=='<') break;
								num = num*10+(all[i].charAt(position2)-'0');
								position2++;
							}
							num--;
							String numb = num+"";
							all[i]=all[i].substring(0,position)+numb+all[i].substring(position2);

							//String query = "update coursevotes set upordown = TRUE where review_id = '"+request.getParameter("reviewid")+"' and user_id = '"+session.getAttribute("Username")+"';";
							String query = "delete from coursevotes where review_id = '"+request.getParameter("reviewid")+"' and user_id = '"+session.getAttribute("Username")+"';";
							st1.executeUpdate(query);
							query = "update coursereview set upvotes = upvotes-1 where review_id='"+request.getParameter("reviewid")+"';";
							st1.executeUpdate(query);
						}
						else if(all[i].contains("Upvote")){
							all[i]=all[i].replaceAll("Upvote", "Upvoted");

							String checking = "value=\"Upvoted\" style=\"background-color:#6495ed; \">:";
							int position = all[i].indexOf(checking);
							position = position+checking.length();
							int position2 = position;
							System.out.println("char is "+all[i].charAt(position));
							int num=0;
							while(true){
								if(all[i].charAt(position2)=='<') break;
								num = num*10+(all[i].charAt(position2)-'0');
								position2++;
							}
							num++;
							String numb = num+"";
							all[i]=all[i].substring(0,position)+numb+all[i].substring(position2);

							String query = "update coursereview set upvotes = upvotes+1 where review_id='"+request.getParameter("reviewid")+"';";
							st1.executeUpdate(query);

							if(all[i].contains("Downvoted")){
								all[i]=all[i].replaceAll("Downvoted","Downvote");

								checking = "value=\"Downvote\" style=\"background-color:#6495ed; \">:";
								position = all[i].indexOf(checking);
								position = position+checking.length();
								position2 = position;
								System.out.println("char is "+all[i].charAt(position));
								num=0;
								while(true){
									if(all[i].charAt(position2)=='<') break;
									num = num*10+(all[i].charAt(position2)-'0');
									position2++;
								}
								num--;
								numb = num+"";
								all[i]=all[i].substring(0,position)+numb+all[i].substring(position2);

								query = "update coursevotes set upordown = TRUE where review_id = '"+request.getParameter("reviewid")+"' and user_id = '"+session.getAttribute("Username")+"';";
								st1.executeUpdate(query);
								query = "update coursereview set downvotes = downvotes-1 where review_id='"+request.getParameter("reviewid")+"';";
								st1.executeUpdate(query);
							}
							else{
								query = "insert into coursevotes values('"+session.getAttribute("Username")+"','"+request.getParameter("reviewid")+"',TRUE);";
								st1.executeUpdate(query);
							}
						}

						System.out.println(i+" "+all[i]);
					}
					ans+=all[i]+"\n";
				}
				session.setAttribute("course_review_code",ans);
				response.sendRedirect("coursereview.jsp");
			}
			else if(bool_course.equals("downvote")){
				System.out.println("Reached in downvotes");
				String text = session.getAttribute("course_review_code").toString();
				String[] all = text.split("\n");
				String sear = request.getParameter("reviewid")+"";
				System.out.println("sear is "+sear);
				sear = "name=\"reviewid\" value="+sear+">";
				String ans="";

				for(int i=0;i<all.length;i++){
					if(all[i].contains(sear)){

						if(all[i].contains("Downvoted")){
							all[i]=all[i].replaceAll("Downvoted", "Downvote");

							String checking = "value=\"Downvote\" style=\"background-color:#6495ed; \">:";
							int position = all[i].indexOf(checking);
							position = position+checking.length();
							int position2 = position;
							System.out.println("char is "+all[i].charAt(position));
							int num=0;
							while(true){
								if(all[i].charAt(position2)=='<') break;
								num = num*10+(all[i].charAt(position2)-'0');
								position2++;
							}
							num--;
							String numb = num+"";
							all[i]=all[i].substring(0,position)+numb+all[i].substring(position2);

							String query = "delete from coursevotes where review_id = '"+request.getParameter("reviewid")+"' and user_id = '"+session.getAttribute("Username")+"';";
							st1.executeUpdate(query);
							query = "update coursereview set downvotes = downvotes-1 where review_id='"+request.getParameter("reviewid")+"';";
							st1.executeUpdate(query);
						}
						else if(all[i].contains("Downvote")){
							all[i]=all[i].replaceAll("Downvote", "Downvoted");

							String checking = "value=\"Downvoted\" style=\"background-color:#6495ed; \">:";
							int position = all[i].indexOf(checking);
							position = position+checking.length();
							int position2 = position;
							System.out.println("char is "+all[i].charAt(position));
							int num=0;
							while(true){
								if(all[i].charAt(position2)=='<') break;
								num = num*10+(all[i].charAt(position2)-'0');
								position2++;
							}
							num++;
							String numb = num+"";
							all[i]=all[i].substring(0,position)+numb+all[i].substring(position2);

							String query = "update coursereview set downvotes = downvotes+1 where review_id='"+request.getParameter("reviewid")+"';";
							System.out.println(query);
							st1.executeUpdate(query);

							if(all[i].contains("Upvoted")){
								all[i]=all[i].replaceAll("Upvoted","Upvote");

								checking = "value=\"Upvote\" style=\"background-color:#6495ed; \">:";
								position = all[i].indexOf(checking);
								position = position+checking.length();
								position2 = position;
								System.out.println("char is "+all[i].charAt(position));
								num=0;
								while(true){
									if(all[i].charAt(position2)=='<') break;
									num = num*10+(all[i].charAt(position2)-'0');
									position2++;
								}
								num--;
								numb = num+"";
								all[i]=all[i].substring(0,position)+numb+all[i].substring(position2);

								query = "update coursevotes set upordown = FALSE where review_id = '"+request.getParameter("reviewid")+"' and user_id = '"+session.getAttribute("Username")+"';";
								st1.executeUpdate(query);
								query = "update coursereview set upvotes = upvotes-1 where review_id='"+request.getParameter("reviewid")+"';";
								st1.executeUpdate(query);	
							}
							else{
								query = "insert into coursevotes values('"+session.getAttribute("Username")+"','"+request.getParameter("reviewid")+"',FALSE);";
								st1.executeUpdate(query);
							}
						}

						System.out.println(i+" "+all[i]);
					}
					ans+=all[i]+"\n";
				}
				session.setAttribute("course_review_code",ans);
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

		try{
			comment_id++;
			String comment_text=request.getParameter("Comment");
			HttpSession session = request.getSession();
			String user_id = session.getAttribute("Username").toString();
			String review_id = request.getParameter("id");
			String datestamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			String timestamp = new SimpleDateFormat("hh:mm:ss").format(new Date());
			String query="Insert into coursecomments values('"+comment_id+"','"+review_id+"','"+user_id+"','"+comment_text+"','"+datestamp+"','"+timestamp+"');";
			st.executeUpdate(query);
			addCourseReview(request, response,session.getAttribute("course_name").toString());
		}
		catch(Exception e){
			comment_id--;
			e.printStackTrace();
		}
	}

}
