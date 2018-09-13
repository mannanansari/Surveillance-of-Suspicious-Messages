// import servlet package



import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



// import java packages


import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;


public class ChatAdminServlet extends HttpServlet{

String chatRoomPath;
String listRoomsPath;
String chatAdminPath;


public void init() throws ServletException  {

	ServletContext context= getServletContext();
	chatRoomPath=context.getInitParameter("CHATROOM_PATH");
	listRoomsPath=context.getInitParameter("LISTROOMS_PATH");
	chatAdminPath= context.getInitParameter("ADMIN_PATH");
	if(chatRoomPath == null || listRoomsPath == null || chatAdminPath== null)
	{
		throw new UnavailableException("Application unavailable");
	}
}

public void doGet( HttpServletRequest req, HttpServletResponse res) throws IOException,ServletException {
		res.setContentType("text/html");
		PrintWriter out= res.getWriter();
		out.println("<html>");
		out.println("<head> <title> Chat Room Administration </title> </head>");
		out.println("<body>");
		out.println("<h1> Chat room administration </h1>");
		out.println("<body bgcolor='#FFFFE0'>");
		out.println("<form method=\"POST\" action =\"" + res.encodeURL(chatAdminPath)+ "\">");

//check for existing chat rooms

HashMap roomList= (HashMap) getServletContext().getAttribute("roomList");
if(roomList!= null){
	Iterator rooms=roomList.keySet().iterator();

if(!rooms.hasNext()){
	out.println("<p>There are no rooms </p>");
	} else {
		out.println("<p> List of Available chat rooms </p>");

while(rooms.hasNext()) {
	String roomName=(String) rooms.next();
	ChatRoom room=(ChatRoom) roomList.get(roomName);
	out.println("<input type=checkbox name= remove value=' " + room.getName() + "'>" +room.getName() + "<br>");
	}
	}
}


//add fields for adding a room

out.println("<p> Enter a new room and the description <p>");
out.println("<table>");
out.println("<tr><td>Name: </td> <td><input name= roomname "+"size=50></td></tr>");


out.println("<tr> <td> Description: </td>");
out.println("<td><textarea name=roomdescr cols=40 rows=5>");
out.println("</textarea></td> </tr>");
out.println("</table>");

//add submit button
out.println("<p><input type=submit value='Update List'>");
out.println("<p><a href=\"" + listRoomsPath +"\"> Chat Now</a>");
out.println("</form>");
out.println("</body></html>");
out.close();
}

public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException,ServletException {
 HashMap roomList = null;
// check for exisiting chat rooms
synchronized(getServletContext()){
	roomList=(HashMap) getServletContext().getAttribute("roomList");
	if(roomList==null)
	{
		roomList=new HashMap();
getServletContext().setAttribute("roomList" ,roomList);
}
}
//Update the room list
String[] removeList=req.getParameterValues("remove");
synchronized(roomList) {
if(removeList !=null) {
for(int i=0;i<removeList.length;i++) {
roomList.remove(removeList[i]);
}
}
}

String roomName=req.getParameter("roomname");

String roomDescr =req.getParameter("roomDescr");
if(roomName != null && roomName.length() >0) {
synchronized(roomList) {
roomList.put(roomName, new ChatRoom(roomName , roomDescr));
}
}
doGet(req ,res);
}
}
