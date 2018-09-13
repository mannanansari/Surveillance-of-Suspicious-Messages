import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
//import javax.sql.DataSource;
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
public class MessageLogFilter implements Filter
{
public void init(FilterConfig u){}

public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain)
               throws IOException,ServletException{

//Invoke the next filer
chain.doFilter(request,response);
//Get the session
HttpSession session =((HttpServletRequest) request).getSession();
//Get the chat message from therequest
String message =request.getParameter("msg");
//Get the profile name from the session
String profileName =(String) session.getAttribute("profileName");
//Get the name of the room from the sesseion
String roomName=(String) session.getAttribute("roomName");
//Create a timestamp
Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
//Get a connection to the database
Connection connection = null;
Statement stmt = null;
String insertStatementStr="INSERT INTO MESSAGE_LOG"+
"(PROFILE_NAME,ROOM_NAME,MESSAGE,TIME_STAMP)VALUES(?,?,?,?)";
try
{
//InitialContext initial=new InitialContext();
//DataSource ds=(DataSource)initial.lookup("jdbc/chat");
//connection = ds.getConnection();
Class.forName("org.postgresql.Driver");
connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testdb",
            "postgres", "admin");
PreparedStatement insertStatement = connection.prepareStatement(insertStatementStr);
insertStatement.setString(1, profileName);
insertStatement.setString(2, roomName);
insertStatement.setString(3, message);
insertStatement.setTimestamp(4, timeStamp);
insertStatement.executeUpdate();
String StrSql="DELETE FROM MESSAGE_LOG WHERE MESSAGE IS NULL";
stmt = connection.createStatement();
stmt.executeUpdate(StrSql);
}
//catch (NamingException ne){
//throw new ServletException("JNDI error" ,ne);
//}
catch (Exception sqle) 
{
throw new ServletException("Database error",sqle);
}
finally
{
if(connection != null)
{
try
{
connection.close();
stmt.close();
}
catch (SQLException sqle) {}
}
}
}
public void destroy() {}
}
