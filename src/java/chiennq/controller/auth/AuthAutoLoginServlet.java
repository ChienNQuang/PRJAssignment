/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chiennq.controller.auth;

import chiennq.registration.RegistrationBLO;
import chiennq.registration.RegistrationDAO;
import chiennq.registration.RegistrationDTO;
import chiennq.utils.MyAppConstants;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Properties;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author Ezarp
 */
public class AuthAutoLoginServlet extends HttpServlet {
    
    private final Logger LOGGER = Logger.getLogger(AuthAutoLoginServlet.class);
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, 
                                    HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext context = this.getServletContext();
        Properties siteMapProperties = (Properties) context.getAttribute("SITE_MAP");
        String url = siteMapProperties.getProperty(MyAppConstants.LoginFeatures.LOGIN_PAGE);
        
        RegistrationDAO registrationDAO = new RegistrationDAO();
        RegistrationBLO blo = new RegistrationBLO(registrationDAO);
        
        boolean hasServerError = false;
        try {
            
            //Check if session exists
            HttpSession session = request.getSession(false);
            if (session != null) {
                //Check if user exist in session and if this user is still valid in DB
                RegistrationDTO user = (RegistrationDTO) session.getAttribute("USER");
                boolean userValid = blo.checkUserValid(user);
                if (!userValid) return;

                //dispatch to respective search page
                if (user.isRole()) {
                    url = siteMapProperties.getProperty(MyAppConstants.SearchAccountFeatures.SEARCH_AS_ADMIN_PAGE);
                } else {
                    url = siteMapProperties.getProperty(MyAppConstants.SearchAccountFeatures.SEARCH_AS_USER_PAGE);
                }
                return;
            }
            
            Cookie[] cookies = request.getCookies();
            if (cookies == null) return;

            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("user")) {
                    String[] parts = cookie.getValue().split(",");
                    String username = parts[0];
                    String hashedPassword = parts[1];
                    RegistrationDTO user = blo.checkLogin(username, hashedPassword);

                    if (user == null) return;

                    if (user.isRole()) {
                        url = siteMapProperties.getProperty(MyAppConstants.SearchAccountFeatures.SEARCH_AS_ADMIN_PAGE);
                    } else {
                        url = siteMapProperties.getProperty(MyAppConstants.SearchAccountFeatures.SEARCH_AS_USER_PAGE);
                    }
                    break;
                }
            }
            
        } catch (SQLException | NamingException ex) {
            LOGGER.error(ex);
            hasServerError = true;
        } finally {
            if (hasServerError){
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } else {
                RequestDispatcher rd = request.getRequestDispatcher(url);
                rd.forward(request, response);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
