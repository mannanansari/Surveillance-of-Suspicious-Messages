import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.List;

public class ModeratedRequest extends HttpServletRequestWrapper {
 List moderatedParameters;
public ModeratedRequest (HttpServletRequest request,List moderatedParameter) {

super(request);
this.moderatedParameters=moderatedParameters;
}

public String getParameter(String parameterName) {
// if this parameter is one of the parameter being moderated,
//it's an attribute.Else,it is a request parameter
//if(moderatedParameters.contains(parameterName)) {
return (String)super.getAttribute(parameterName);
//}
//else{
//return super.getParameter(parameterName);
//}
}
}
 
