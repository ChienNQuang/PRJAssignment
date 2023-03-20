/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chiennq.controller.auth;

import chiennq.registration.RegistrationBLO;
import chiennq.registration.RegistrationCreateError;
import chiennq.registration.RegistrationDAO;
import chiennq.utils.MyAppConstants;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Properties;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author Ezarp
 */
public class AuthRegisterServlet extends HttpServlet {

    private final Logger LOGGER = Logger.getLogger(AuthRegisterServlet.class);
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RegistrationDAO registrationDAO = new RegistrationDAO();
        RegistrationBLO blo = new RegistrationBLO(registrationDAO);
        
        String username = request.getParameter("txtUsername");
        String password = request.getParameter("txtPassword");
        String confirm = request.getParameter("txtConfirm");
        String fullname = request.getParameter("txtFullname");
        
        boolean hasServerError = false;
        RegistrationCreateError errors = new RegistrationCreateError();
        
        ServletContext context = this.getServletContext();
        Properties siteMapProperties = (Properties) context.getAttribute("SITE_MAP");
        String url = siteMapProperties.getProperty(MyAppConstants.RegisterFeatures.REGISTER_RESULT_PAGE);

        try {
            boolean result = blo.createAccount(username, password, confirm, fullname, errors);

            if (result) {
                url = siteMapProperties.getProperty(MyAppConstants.RegisterFeatures.LOGIN_PAGE);
            }//end insert success
            else {
                request.setAttribute("CREATE_ERRORS", errors);
            }
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            LOGGER.error(ex);
            if (msg.contains("duplicate")) {
                errors.setUsernameIsExisted(username + " already exists");
                request.setAttribute("CREATE_ERRORS", errors);
            }//duplicate username
        } catch (NamingException | NoSuchAlgorithmException ex) {
            LOGGER.error(ex);
            hasServerError = true;
        } finally {
            if (hasServerError) {
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
