//Import servlet package
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
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

public class ChatRoomsServlet extends HttpServlet {
String chatRoomPath;
String listRoomsPath;
public void init() throws ServletException {
ServletContext context =getServletContext();
chatRoomPath=context.getInitParameter("CHATROOM_PATH");
listRoomsPath = context.getInitParameter("LISTROOMS_PATH");
if(chatRoomPath == null || listRoomsPath == null) {
throw new UnavailableException("Application unavialble.");
}
}


public void doGet(HttpServletRequest req,HttpServletResponse res)
throws IOException,ServletException{

res.setContentType("text/html");
PrintWriter out =res.getWriter();

ChatRoom room = getRoom(req ,res);
if(room == null) {
throw new ServletException("Room not found");
}

String listPar = req.getParameter("list");
if(listPar != null && listPar.equals("true")) {
 writeMessage(out ,room ,getProfileName(req));
}

else
{
out.println("<html>");
out.println("<body>");
out.println("<form method=\"post\" action=\""+ res.encodeURL(chatRoomPath) + "\" target=\"_top\">");
out.println("<p>Enter your message:</p>");
out.println("<input name=\"msg\" size=\"30\">");

//Add a Submit button
out.println("<p><input type=submit value='Send Message'>");

//Add a exit button
out.println("</form>");
out.println("<form action=\"" + res.encodeURL(listRoomsPath) + "\" method=\"get\" target=\"_top\">");
out.println("<input type=submit value=Exit>");
out.println("</form>");
out.println("</body></html>");
}
out.close();

}


public void doPost(HttpServletRequest req,HttpServletResponse res) 
  throws IOException,ServletException{
res.setContentType("text/html");

ChatRoom room = getRoom(req,res);
if(room == null) {
 throw new ServletException("Room not found");

}
String profilename =getProfileName(req);

String msg =req.getParameter("msg");
if(msg !=null && msg.length() != 0) {
room.addChatEntry(new ChatEntry(profilename,msg));

}
writeFrame(res ,room);
}


private String getProfileName(HttpServletRequest req) {
HttpSession session =req.getSession(true);
String profileName = (String) session.getAttribute("profileName");
if (profileName ==null) {
profileName = req.getParameter("profileName");
if(profileName == null || profileName.length() == 0) {
profileName ="A Spineless spy";
}
session.setAttribute("profileName", profileName);
}
else
{

String newName =req.getParameter("profileName");
if(newName != null && newName.length()>0 && !newName.equals("profileName")){
profileName=newName;
session.setAttribute("profileName",profileName);

}
}
return profileName;

}

private ChatRoom getRoom(HttpServletRequest req,HttpServletResponse res) throws IOException {

HttpSession session =req.getSession(true);
PrintWriter out= res.getWriter();

String roomName = (String) session.getAttribute("roomName");
if(roomName ==null)
{
//just  Enterd?

roomName=req.getParameter("roomName");
if(roomName ==null || roomName.length() == 0) {

writeError(out,"room not specified");

return null;
}

session.setAttribute("roomName", roomName);
}

else
{
// Entered a new room?
String newRoom =req.getParameter("roomName");
if(newRoom != null && newRoom.length() > 0 &&!newRoom.equals(roomName)) {
roomName = newRoom;
session.setAttribute("roomName", roomName);
}
}

HashMap roomList = (HashMap) getServletContext().getAttribute("roomList");
ChatRoom room =(ChatRoom) roomList.get(roomName);
if (room == null) {
writeError(out,"room " + roomName + "not found");
return null;
}
return room;
}

private void writeError(PrintWriter out,String msg) {
out.println("<html>");
out.println("<head><title>error</title></head>");
out.println("<body>");
out.println("<h1>Error</h1>");
out.println(msg);
out.println("</body></html>");
}

private void writeFrame(HttpServletResponse res,ChatRoom room) throws IOException {
PrintWriter out=res.getWriter();
out.println("<html>");
out.println("<head><title>" + room.getName() +"</title></head>");
out.println("<frameset rows ='50%,50%' border=0 frameborder=no>");
out.println("<frame src=\"" +res.encodeURL(chatRoomPath)
           + "?list=true\" name=\"list\" scrolling=\"auto\">");
out.println("<frame src=\"" +res.encodeURL(chatRoomPath)  
           + "?list=false\" name=\"form\" scrolling=\"auto\">");
out.println("<noframes>");
out.println("<body>");
out.println("Viewing this page requires a browser capeable of" + "displaying frames.");
out.println("</body>");
out.println("<noframes>");
out.println("</frameset>");
out.println("</html>");
out.close();
}

private void  writeMessage(PrintWriter out,ChatRoom room, String profileName) {
StringBuffer sb = new StringBuffer();
out.println("<html>");
out.println("<head><meta http-equiv=\"refresh\" content =\"5\"></head>");
out.println("<body>");
out.println("<b>Room: " +room.getName() + "</b><br>" + "<b>Identity:" +profileName + "</b><br>");
//list msgs in the roo0m
if (room.size() == 0)  {
out.println("<font color=red>There are no messages in this room " + "yet</font>");

}
else
{
Iterator entries =room.iterator();
while (entries.hasNext()) {
ChatEntry entry =(ChatEntry) entries.next();
 
String entryName = entry.getProfileName();
if(entryName.equals(profileName)) {
out.print("<font color=blue>");
}
out.println(entryName + " : " + entry.getMessage() +"<br>");
if(entryName.equals(profileName)){
out.print("</font>");
}
}
}
out.println("</body></html>");
}
}
