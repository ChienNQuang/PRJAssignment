/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chiennq.controller.cart;

import chiennq.receipt.ReceiptObj;
import chiennq.cart.CartObj;
import chiennq.detail.DetailDAO;
import chiennq.order.OrderBLO;
import chiennq.order.OrderCheckoutError;
import chiennq.order.OrderDAO;
import chiennq.product.ProductDAO;
import chiennq.utils.MyAppConstants;
import java.io.IOException;
import java.io.PrintWriter;
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
public class CartCheckoutServlet extends HttpServlet {

    private final Logger LOGGER = Logger.getLogger(CartCheckoutServlet.class);
    
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
        ProductDAO productDAO = new ProductDAO();
        OrderDAO orderDAO = new OrderDAO();
        DetailDAO detailDAO = new DetailDAO();
        OrderBLO blo = new OrderBLO(productDAO, orderDAO, detailDAO);
        
        String name = request.getParameter("txtName");
        String address = request.getParameter("txtAddress");
        
        ServletContext context = this.getServletContext();
        Properties siteMapProperties = (Properties) context.getAttribute("SITE_MAP");
        String url = siteMapProperties.getProperty(
                MyAppConstants.CheckoutFeatures.VIEW_CART);
        
        OrderCheckoutError errors = new OrderCheckoutError();
        boolean hasServerError = false;
        try {
            HttpSession session = request.getSession(false);
            if (session == null) return;
            
            CartObj cart = (CartObj) session.getAttribute("CART");
            if (cart == null) return;
            String orderId = blo.checkout(cart, name, address, errors);
            
            if (orderId != null) {
                session.setAttribute("CART", null);
                url = siteMapProperties.getProperty(MyAppConstants.CheckoutFeatures.RECEIPT_ACTION) 
                        + "?orderId=" + orderId;
            } else {
                request.setAttribute("CHECKOUT_ERRORS", errors);
            }
        } catch (NamingException | SQLException ex) {
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
