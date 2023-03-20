/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chiennq.controller.account;

import chiennq.registration.RegistrationBLO;
import chiennq.registration.RegistrationDAO;
import chiennq.registration.RegistrationDTO;
import chiennq.registration.RegistrationDeleteError;
import chiennq.utils.MyAppConstants;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author Ezarp
 */
public class AccountDeleteAccountServlet extends HttpServlet {

    private final Logger LOGGER = Logger.getLogger(AccountDeleteAccountServlet.class);
    
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
        
        String username = request.getParameter("pk");
        String lastSearchValue = request.getParameter("lastSearchValue");
        
        ServletContext context = this.getServletContext();
        Properties siteMapProperties = (Properties) context.getAttribute("SITE_MAP");
        String resourceRewriting = MyAppConstants.DeleteAccountFeatures.SEARCH_ACTION;
        boolean hasServerError = false;
        RegistrationDeleteError errors = new RegistrationDeleteError();
        try {
            HttpSession session = request.getSession(false);
            RegistrationDTO user = (RegistrationDTO) session.getAttribute("USER");
            if (user.getUsername().equals(username)) {
                errors.setDeleteSelfError("Cannot delete yourself!");
                
                request.setAttribute("DELETE_ERRORS", errors);
            } else {
                boolean result = blo.deleteAccount(username);
                if (result) {
                    LOGGER.info("User " + username + " deleted!");
                }
            }
        } catch (SQLException | NamingException ex) {
            LOGGER.error(ex);
            hasServerError = true;
        } finally {
            if (hasServerError) {
                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } else {
                String url = siteMapProperties.getProperty(resourceRewriting) +
                            "?txtSearchValue=" + lastSearchValue;
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
