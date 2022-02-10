package site.alexkononsol.siteToOK.util;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

@WebListener
@Component
public class MyRequestContextListener extends RequestContextListener {
    //the method is designed to get the URL of the current request
    public String getMyUrl(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getRequestURL().toString().replace(request.getServletPath(),"");
    }
}
