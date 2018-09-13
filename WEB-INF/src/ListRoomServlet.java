//Import servlet package
import javax.servlet.ServletException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


// Import java packges
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.net.URLEncoder;

public class ListRoomServlet extends HttpServlet {

String chatAdminPath;
String listRoomsPath;
String chatRoomPath;

public void init() throws ServletException  {

	ServletContext context= getServletContext();
	chatRoomPath=context.getInitParameter("CHATROOM_PATH");
	listRoomsPath=context.getInitParameter("LISTROOMS_PATH");
	chatAdminPath= context.getInitParameter("ADMIN_PATH");
	if(chatRoomPath == null || listRoomsPath == null || chatAdminPath== null)
	{
			
	}
}

public void doGet(HttpServletRequest req,HttpServletResponse res)
throws IOException {

res.setContentType("text/html");
PrintWriter out =res.getWriter();
	String expand = req.getParameter("expand");
HttpSession session =req.getSession(true);
String profileName = (String) session.getAttribute("profileName");
if (profileName ==null) {
profileName = "";
}
out.println("<html>");
out.println("<head><title>Chat rooms</title></head>");
out.println("<body>");
out.println("<h1>Chat rooms</h1>");
out.println("<body bgcolor='#FFFFE0'>");
out.println("<form method= POST action=\"" + chatRoomPath + "\">");
out.println("<p><a href=\"" + chatAdminPath + "\">Create Rooms </a></p>");

//get the list of the rooms
 HashMap roomList = (HashMap) getServletContext().getAttribute("roomList");
if(roomList == null) {
out.println("<p>there are no rooms available rightnow.</p>");
}

else
{
// Add radio boxes for selecting a room 
out.println("Select the room you would like to enter," +"or click on a name to see the description:<p>");
Iterator rooms = roomList.keySet().iterator();
boolean isFirst = true;
while (rooms.hasNext()) {
String roomName = (String) rooms.next();
ChatRoom room = (ChatRoom) roomList.get(roomName);
String listRoomsURL = listRoomsPath + "/?expand=" + URLEncoder.encode(roomName);
listRoomsURL = res.encodeURL(listRoomsURL);

out.println("<input type=radio name=roomName value=\"" + roomName +"\"" +(isFirst ? "CHECKED" : "") +">" + "<a href=\"" +listRoomsURL + "\">" +roomName +"</a><br>");
isFirst = false;

// Show description if requested 

if(expand != null && expand.equals(roomName)) {
out.println("<blockquote><br>");
if(room.getDescription().length()==0)
	out.println("No Room Description available");
	else
		out.println(room.getDescription());
out.println("<blockquote><br>");

}
}
//add a field for for the profile name
out.println("<p>Enter your name:");
out.println("<input name=profileName value='" + profileName + "' size=30>");
//Add submit button
out.println("<p><input type=submit value='Enter'>");
out.println("</form>");
}

out.println("</body><html>");
out.close();
}
}


