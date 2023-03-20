/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chiennq.controller.account;

import chiennq.registration.RegistrationBLO;
import chiennq.registration.RegistrationDAO;
import chiennq.registration.RegistrationDTO;
import chiennq.utils.MyAppConstants;
import java.io.IOException;
import java.sql.SQLException;
import javax.naming.NamingException;
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
public class AccountUpdateAccountServlet extends HttpServlet {

    private final Logger LOGGER = Logger.getLogger(AccountUpdateAccountServlet.class);
    
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
        String roleParam = request.getParameter("chkRole");
        String lastPageNumber = request.getParameter("lastPageNumber");
        String lastSearchValue = request.getParameter("lastSearchValue");
        
        boolean hasServerError = false;
        String resourceRewriting = MyAppConstants.UpdateAccountFeatures.SEARCH_ACTION;
        boolean role = roleParam != null;
        try {
            boolean updateResult = blo.updateAccountRole(username, role);
            
            if (!updateResult) return;
            
            HttpSession session = request.getSession(false);
            RegistrationDTO user = (RegistrationDTO) session.getAttribute("USER");
            
            if (user.getUsername().equals(username)) {
                user = blo.getRegistrationByUsername(username);
                session.setAttribute("USER", user);
                resourceRewriting = MyAppConstants.AutoLoginFeatures.AUTO_LOGIN_ACTION;
            } else {
                resourceRewriting = MyAppConstants.UpdateAccountFeatures.SEARCH_ACTION + 
                    "?txtSearchValue=" + lastSearchValue 
                    + "&pageNumber=" + lastPageNumber;
            }
        } catch (SQLException | NamingException ex) {
            LOGGER.error(ex);
            hasServerError = true;
        } finally {
            if (hasServerError) {
                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } else {
                response.sendRedirect(resourceRewriting);
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
