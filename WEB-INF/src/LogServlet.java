import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class LogServlet extends HttpServlet 
{
public void doPost(HttpServletRequest request, HttpServletResponse response)
{
try
{
response.setContentType("text/html");
PrintWriter out = response.getWriter();
String n=request.getParameter("emil");
String p=request.getParameter("pswd");
if(validate(n, p))
{
RequestDispatcher rd=request.getRequestDispatcher("main.html");
rd.forward(request,response);
}
else
{
out.print("Sorry username or password error");
RequestDispatcher rd=request.getRequestDispatcher("index.html");
rd.include(request,response);
}
out.close();
}
catch (Exception e) 
{
System.err.println( e.getClass().getName()+": "+ e.getMessage() );
System.exit(0);
}
}
public static boolean validate(String name,String pass)
{
boolean status=false;
try
{
Connection con = null;
PreparedStatement ps = null;
Class.forName("org.postgresql.Driver");
con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testdb","postgres", "admin");
ps=con.prepareStatement("select EMAIL,PASWD from DATA where EMAIL=? and PASWD=?");
ps.setString(1,name);
ps.setString(2,pass);
ResultSet rs=ps.executeQuery();
status=rs.next();
}
catch(Exception e)
{
System.out.println(e);
}
return status;
}
}