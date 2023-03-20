/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chiennq.utils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author Ezarp
 */
public class DBHelper implements Serializable{
    private DBHelper() {
    }
    
    public static Connection getConnection() 
            throws  SQLException, NamingException {
        //get current context
        Context currentContext = new InitialContext();
        //look up tomcat
        Context tomcatContext = (Context) currentContext.lookup("java:comp/env");
        //look up DS
        DataSource ds = (DataSource) tomcatContext.lookup("MyDB");
        //get connection
        Connection con = ds.getConnection();
        return con;
    }
}