

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	Statement st1=null;
	int instructor_review=1;
	int comment_id=1;
	public void init() throws ServletException {
		String dbURL2 = "jdbc:postgresql://localhost/cs387";
		String user = "deepanjan";
		String pass = "najnapeed";
		try {
			Class.forName("org.postgresql.Driver");
			conn1 = DriverManager.getConnection(dbURL2, user, pass);
			st = conn1.createStatement();
			st1 = conn1.createStatement();
			ResultSet rs;
			rs = st.executeQuery("Select count(*) from instructorcomments;");
			rs.next();
			comment_id=rs.getInt(1);
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
	protected void addReview(HttpServletRequest request, HttpServletResponse response,String instructor_name) throws ServletException, IOException {
		HttpSession session = request.getSession();
		try{
			ResultSet rs;
			rs=st.executeQuery("Select instructor_id from instructor where name='"+instructor_name+"';");
			rs.next();
			String instructor_id=rs.getString(1);
			session.setAttribute("instructor_id",instructor_id);
			rs=st.executeQuery("Select user_id,review_text,review_id from instructorreview where instructor_id='"+
					instructor_id.toString()+"' order by upvotes-downvotes desc limit "+instructor_review+";");
			int i=0;
			String review_text="";
			while(rs.next()){
				System.out.println("instructor review");
				String reviewid = rs.getString(3);
				ResultSet newrs;
				String upquery = "Select UporDown from instructorvotes where user_id='"+session.getAttribute("Username")+"' and review_id='"+reviewid+"';";
				System.out.println("upquery is "+upquery);
				newrs=st1.executeQuery(upquery);
				String up = "Upvote";
				String down = "Downvote";
				while(newrs.next()){
					System.out.println("reached reached "+newrs.getBoolean(1));
					if(newrs.getBoolean(1)) up = "Upvoted";
					else down="Downvoted";
				}
				upquery = "Select upvotes,downvotes from instructorreview where review_id='"+reviewid+"';";
				newrs=st1.executeQuery(upquery);
				newrs.next();
				int upvote = newrs.getInt(1);
				int downvote = newrs.getInt(2);
				System.out.println(upvote);
				System.out.println(downvote);
				String review_comment="<div style=\"float:left;margin-left:125px;color:black;padding: 10px;" +
				"text-align: left;width:1030px;border: 1px solid navy;background-color:#f1ffff \">Recent Comments</div>";
				String comment_query="Select comment_text,user_id from instructorcomments where review_id='"+
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
				"</b></i></span><p>"+rs.getString(2)+"</p></div><div style = \"float:left;margin-left:10px\"><div "+
				"style = \"float:top;margin-left:10px\">"+
				"<form action=\"instructor\" method=\"get\"><input type=\"submit\"  value=\""+up+"\" style"+
				"=\"background-color:#6495ed; \">:"+upvote+"<input type=\"hidden\" name=\"reviewid\" value="+rs.getString(3)+">"+
				" <input type=\"hidden\" name=\"select_dept\" value=\"upvote\"></form> </div>"+
				"<div style = \"float:top;margin-top:20px\">"+
				"<form action=\"instructor\" method=\"get\"><input type=\"submit\"  value=\""+down+"\" style"+
				"=\"background-color:#6495ed; \">:"+downvote+"<input type=\"hidden\" name=\"reviewid\" value="+reviewid+">"+
				" <input type=\"hidden\" name=\"select_dept\" value=\"downvote\"></form> </div></div></div><br><br><br>\n"
				+review_comment+"<div style=\"float:top;margin-top:10px;width:1000px;margin-left:125px;padding:25px;border:1px solid navy;background-color:#ccffff\">" +
				"<form action=\"instructor\" method=\"post\" style=\"margin-left:100px\">Enter your comment:<br><textarea style=\"width:800px\" type=\"text\" name=\"Comment\"></textarea><br>" +
				"<input type=\"hidden\" name=\"id\" value=\""+reviewid+"\"><input type=\"submit\"style"+
				"=\"background-color:#6495ed;\" name=\"CourseComment\" value=\"Submit Comment\"></form></div></div>\t";
				;
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

			String query2 = "Select rating from instructor where instructor_id='"+instructor_id+"';";
			//System.out.println("query2 is "+query2);
			rs=st.executeQuery(query2);
			rs.next();

			session.setAttribute("instructor_review_code", review_text);
			session.setAttribute("instructor_rating", rs.getString(1));
			response.sendRedirect("instructorreview.jsp");
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
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
				retval+="</select></div><div style=\"margin-left:300px\"><input type=\"submit\" style=\"background-color:#6495ed;\" value=\"Select\"><input type=\"hidden\" name=\"select_dept\" value=\"instructor_page\"></div></div></form>";
				session.setAttribute("instructors",retval);
				//System.out.println(retval);
				response.sendRedirect("instructor.jsp");
			}
			else if(bool_instructor.equals("instructor_page") || bool_instructor.equals("seeMoreReview")){

				String instructor_name=(String)request.getParameter("instructors");
				if(instructor_name.equals("123456")){
					instructor_name=(String)session.getAttribute("instructor_name");
				}
				if(bool_instructor.equals("seeMoreReview") ){
					instructor_review+=10;
					instructor_name=(String)session.getAttribute("instructor_name");
				}
				System.out.println(instructor_name);
				session.setAttribute("instructor_name",instructor_name);
				addReview(request, response, instructor_name);
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
				ResultSet rs;
				String str = "select sum(rating),count(rating) from instructorrating where instructor_id='"+instructor+"';";
				System.out.println(str);
				rs = st.executeQuery(str);
				rs.next();
				double rat = 0;
				double firnum = rs.getInt(1);
				double secondnum = rs.getInt(2);
				if(rs.getInt(2)>0) rat = firnum/secondnum;
				DecimalFormat df = new DecimalFormat("#.##");
				str = "update instructor set rating = "+df.format(rat)+" where instructor_id='"+instructor+"';";
				System.out.println(str);
				st.executeUpdate(str);
				
				session.setAttribute("instructor_rating",df.format(rat)+"");

				System.out.println("Finally");
				response.sendRedirect("instructorreview.jsp");
			}
			else if(bool_instructor.equals("upvote")){
				System.out.println("Reached in upvotes in instructor");
				String text = session.getAttribute("instructor_review_code").toString();
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

							String query = "delete from instructorvotes where review_id = '"+request.getParameter("reviewid")+"' and user_id = '"+session.getAttribute("Username")+"';";
							st1.executeUpdate(query);
							query = "update instructorreview set upvotes = upvotes-1 where review_id='"+request.getParameter("reviewid")+"';";
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

							String query = "update instructorreview set upvotes = upvotes+1 where review_id='"+request.getParameter("reviewid")+"';";
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

								query = "update instructorvotes set upordown = TRUE where review_id = '"+request.getParameter("reviewid")+"' and user_id = '"+session.getAttribute("Username")+"';";
								st1.executeUpdate(query);
								query = "update instructorreview set downvotes = downvotes-1 where review_id='"+request.getParameter("reviewid")+"';";
								st1.executeUpdate(query);

							}
							else{
								query = "insert into instructorvotes values('"+session.getAttribute("Username")+"','"+request.getParameter("reviewid")+"',TRUE);";
								System.out.println("query "+query);
								st1.executeUpdate(query);
							}
						}


					}
					System.out.println(i+" "+all[i]);
					ans+=all[i]+"\n";
				}
				session.setAttribute("instructor_review_code",ans);
				response.sendRedirect("instructorreview.jsp");
			}
			else if(bool_instructor.equals("downvote")){
				System.out.println("Reached in downvotes in instructor");
				String text = session.getAttribute("instructor_review_code").toString();
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

							String query = "delete from instructorvotes where review_id = '"+request.getParameter("reviewid")+"' and user_id = '"+session.getAttribute("Username")+"';";
							st1.executeUpdate(query);
							query = "update instructorreview set downvotes = downvotes-1 where review_id='"+request.getParameter("reviewid")+"';";
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

							String query = "update instructorreview set downvotes = downvotes+1 where review_id='"+request.getParameter("reviewid")+"';";
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

								query = "update instructorvotes set upordown = FALSE where review_id = '"+request.getParameter("reviewid")+"' and user_id = '"+session.getAttribute("Username")+"';";
								st1.executeUpdate(query);
								query = "update instructorreview set upvotes = upvotes-1 where review_id='"+request.getParameter("reviewid")+"';";
								st1.executeUpdate(query);
							}
							else{
								query = "insert into instructorvotes values('"+session.getAttribute("Username")+"','"+request.getParameter("reviewid")+"',FALSE);";
								st1.executeUpdate(query);
							}
						}

					}
					//System.out.println(i+" "+all[i]);
					ans+=all[i]+"\n";
				}
				session.setAttribute("instructor_review_code",ans);
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
		try{
			comment_id++;
			String comment_text=request.getParameter("Comment");
			HttpSession session = request.getSession();
			String user_id = session.getAttribute("Username").toString();
			String review_id = request.getParameter("id");
			String datestamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			String timestamp = new SimpleDateFormat("hh:mm:ss").format(new Date());
			String query="Insert into instructorcomments values('"+comment_id+"','"+review_id+"','"+user_id+"','"+comment_text+"','"+datestamp+"','"+timestamp+"')";
			st.executeUpdate(query);
			addReview(request, response,session.getAttribute("instructor_name").toString());
		}
		catch(Exception e){
			comment_id--;
			e.printStackTrace();
		}
	}

}
