import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.io.*;
public class RegisterServlet extends HttpServlet
{
public void doPost(HttpServletRequest request, HttpServletResponse response) 
{
try 
{
response.setContentType("text/html");
Connection c = null;
Statement stmt = null;
PrintWriter out=response.getWriter();
String fname=request.getParameter("Name");
String lname=request.getParameter("LastName");
String email=request.getParameter("Email");
String passwd=request.getParameter("Password");
String gen=request.getParameter("radiobutton");
Class.forName("org.postgresql.Driver");
c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testdb","postgres", "admin");
c.setAutoCommit(false);
String StrSql="insert into DATA values('"+fname+"','"+lname+"','"+email+"','"+passwd+"','"+gen+"')";
stmt = c.createStatement();
int k = stmt.executeUpdate(StrSql);
if(k!=0)
{
RequestDispatcher rd=request.getRequestDispatcher("main.html");
rd.forward(request,response);
}
else
{
RequestDispatcher rd=request.getRequestDispatcher("new.html");
rd.forward(request,response);
}
stmt.close();
c.commit();
c.close();
}
catch (Exception e) 
{
System.err.println( e.getClass().getName()+": "+ e.getMessage() );
System.exit(0);
}
}
}