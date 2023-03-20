/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chiennq.filter;

import chiennq.registration.RegistrationBLO;
import chiennq.registration.RegistrationDAO;
import chiennq.registration.RegistrationDTO;
import chiennq.utils.MyAppConstants;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import javax.naming.NamingException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author Ezarp
 */
public class AuthenticationFilter implements Filter {
    private final Logger LOGGER = Logger.getLogger(AuthenticationFilter.class);
    private static final boolean debug = true;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;
    
    public AuthenticationFilter() {
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        //This filter check for what role the user is 
        
        RegistrationDAO registrationDAO = new RegistrationDAO();
        RegistrationBLO blo = new RegistrationBLO(registrationDAO);
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        boolean hasServerError = false;
        try {
            HttpSession session = req.getSession(false);
            if (session != null) {
                //Check if user exist in session and if this user is still valid in DB
                RegistrationDTO user = (RegistrationDTO) session.getAttribute("USER");
                boolean userValid = blo.checkUserValid(user);
                
                if (!userValid) {
                    request.setAttribute("USER_ROLE", "guest");
                    return;
                }
                
                if (user.isRole()) {
                    request.setAttribute("USER_ROLE", "admin");
                    return;
                } 
                
                if (!user.isRole()) {
                    request.setAttribute("USER_ROLE", "user");
                    return;
                }
            }
            
            Cookie[] cookies = req.getCookies();
            if (cookies == null) {
                request.setAttribute("USER_ROLE", "guest");
                return;
            }
            Cookie userCookie = getUserCookie(cookies);

            if (userCookie == null) {
                request.setAttribute("USER_ROLE", "guest");
                return;
            }

            String username = userCookie.getValue().split(",")[0];
            String hashedPassword = userCookie.getValue().split(",")[1];

            RegistrationDTO user = blo.checkLogin(username, hashedPassword);

            if (user == null) {
                request.setAttribute("USER_ROLE", "guest");
                return;
            }

            if (user.isRole()) {
                request.setAttribute("USER_ROLE", "admin");
                return;
            } 

            if (!user.isRole()) {
                request.setAttribute("USER_ROLE", "user");
                return;
            }
        } catch (SQLException | NamingException ex) {
            LOGGER.error(ex);
            hasServerError = true;
        } finally {
            if (hasServerError) {
                res.sendError(500);
            } else {
                chain.doFilter(request, response);
            }
        }
        
    }
    
    private Cookie getUserCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies)
            if (cookie.getName().equals("user"))
                return cookie;
        return null;
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {        
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {        
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {                
                log("AuthenticationFilter:Initializing filter");
            }
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("AuthenticationFilter()");
        }
        StringBuffer sb = new StringBuffer("AuthenticationFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }
    
    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);        
        
        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);                
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");                
                pw.print(stackTrace);                
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
                LOGGER.error(ex);
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
                LOGGER.error(ex);
            }
        }
    }
    
    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
            Logger.getLogger(AuthenticationFilter.class).error(ex);
        }
        return stackTrace;
    }
    
    public void log(String msg) {
        filterConfig.getServletContext().log(msg);        
    }
    
}
