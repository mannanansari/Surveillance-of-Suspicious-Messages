import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
public class MessageModeratorFilter implements Filter
{
String protocol,cat;
ArrayList wordList,wordListTheft,wordListMurder,wordListKidnap,wordListCorruption,wordListTerror;
ArrayList moderatedParameters;
String warningMessage;
//Get the session
HttpSession session;
public void init(FilterConfig config) throws ServletException
{
try
{
//HttpSession session =((HttpServletRequest) request).getSession();
String fileName = config.getInitParameter("moderated_words_file");
//Read the file containing words for moderation
String fileNamem = config.getInitParameter("moderated_words_file_m");
String fileNameth = config.getInitParameter("moderated_words_file_th");
String fileNamek = config.getInitParameter("moderated_words_file_k");
String fileNamec = config.getInitParameter("moderated_words_file_c");
String fileNametr = config.getInitParameter("moderated_words_file_tr");

InputStream is= config.getServletContext().getResourceAsStream(fileName);
BufferedReader reader = new BufferedReader(new InputStreamReader(is));
wordList =new ArrayList();
//add the words in the array
while(true)
{
String word=reader.readLine();
if(word==null)
{
break;
}
wordList.add(word.toLowerCase());
}
reader.close();
//can directly put the words in arraylist.
InputStream ism= config.getServletContext().getResourceAsStream(fileNamem);
BufferedReader readerm = new BufferedReader(new InputStreamReader(ism));
wordListMurder =new ArrayList();
while(true)
{
String word=readerm.readLine();
if(word==null)
{
break;
}
wordListMurder.add(word.toLowerCase());
}
readerm.close();

//theft
InputStream isth= config.getServletContext().getResourceAsStream(fileNameth);
BufferedReader readerth = new BufferedReader(new InputStreamReader(isth));
wordListTheft =new ArrayList();
while(true)
{
String word=readerth.readLine();
if(word==null)
{
break;
}
wordListTheft.add(word.toLowerCase());
}
readerth.close();

//kidnap
InputStream isk= config.getServletContext().getResourceAsStream(fileNamek);
BufferedReader readerk = new BufferedReader(new InputStreamReader(isk));
wordListKidnap =new ArrayList();
while(true)
{
String word=readerk.readLine();
if(word==null)
{
break;
}
wordListKidnap.add(word.toLowerCase());
}
readerk.close();

//corruption
InputStream isc= config.getServletContext().getResourceAsStream(fileNamec);
BufferedReader readerc = new BufferedReader(new InputStreamReader(isc));
wordListCorruption =new ArrayList();
while(true)
{
String word=readerc.readLine();
if(word==null)
{
break;
}
wordListCorruption.add(word.toLowerCase());
}
readerc.close();

//terror
InputStream istr= config.getServletContext().getResourceAsStream(fileNametr);
BufferedReader readertr = new BufferedReader(new InputStreamReader(istr));
wordListTerror =new ArrayList();
while(true)
{
String word=readertr.readLine();
if(word==null)
{
break;
}
wordListTerror.add(word.toLowerCase());
}
readertr.close();



//Get the warning message. If not found, use a default.
warningMessage=config.getInitParameter("warning_message");
if(warningMessage == null) 
{
warningMessage = "***Message restricted";
}
moderatedParameters =new ArrayList();
moderatedParameters.add("msg");
}
catch(IOException ie)
{
ie.printStackTrace();
throw new ServletException("Error reading moderate_words.txt");
}
catch(Exception  e)
{
e.printStackTrace();
}
}

public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain)
throws IOException, ServletException {
boolean setSus=false;
try
{
//Get a connection to the database
Connection connection =null;
String insertStatementStr="INSERT INTO SUS_MESSAGE_LOG"+
"(PROFILE_NAME,ROOM_NAME,MESSAGE,TIME_STAMP,CATEGORY)VALUES(?,?,?,?,?)";
Class.forName("org.postgresql.Driver");
connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testdb",
            "postgres", "admin");

HttpSession session =((HttpServletRequest) request).getSession();
//Get the profile name from the session
String profileName =(String) session.getAttribute("profileName");
//Get the name of the room from the sesseion
String roomName=(String) session.getAttribute("roomName");
//Create a timestamp
Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
String message=((HttpServletRequest)request).getParameter("msg");
if(message !=null)
{
String[] w;
String delima = "[\\s]+";
String[] strr = message.split(delima);
for(String q : strr)
{
w=Wordnet.word(q);
for (String sp:w)
{
boolean setWarning=false;
for(int i=0; i<wordList.size();i++)
{
if((sp.toLowerCase()).indexOf((String)wordList.get(i))!=-1)
{
setWarning=true;
break;
}
}

//theft
for(int i=0; i<wordListTheft.size();i++)
{
if((sp.toLowerCase()).indexOf((String)wordListTheft.get(i))!=-1)
{
cat="Theft";
setSus=true;
break;
}
}

//murder
for(int i=0; i<wordListMurder.size();i++)
{
if((sp.toLowerCase()).indexOf((String)wordListMurder.get(i))!=-1)
{
cat="Murder";
setSus=true;
break;
}
}

//kidnap
for(int i=0; i<wordListKidnap.size();i++)
{
if(sp.toLowerCase().indexOf((String)wordListKidnap.get(i))!=-1)
{
cat="Kidnap";
setSus=true;
break;
}
}

//corrpution
for(int i=0; i<wordListCorruption.size();i++)
{
if(sp.toLowerCase().indexOf((String)wordListCorruption.get(i))!=-1)
{
cat="Corruption";
setSus=true;
break;
}
}

//terror
for(int i=0; i<wordListTerror.size();i++)
{
if(sp.toLowerCase().indexOf((String)wordListTerror.get(i))!=-1)
{
cat="Terror";
setSus=true;
break;
}
}

if(setWarning)
{
message=warningMessage;
request.setAttribute("msg",message);
request=new ModeratedRequest((HttpServletRequest)request,moderatedParameters);
}
}

if(setSus)
{
setSus=false;
PreparedStatement insertStatement = connection.prepareStatement(insertStatementStr);
insertStatement.setString(1, profileName);
insertStatement.setString(2, roomName);
insertStatement.setString(3, message);
insertStatement.setTimestamp(4, timeStamp);
insertStatement.setString(5, cat);
insertStatement.executeUpdate();
}
}
}
}
catch(Exception  e)
{
e.printStackTrace();
}
chain.doFilter(request,response);
}
public void destroy()
{
}
}