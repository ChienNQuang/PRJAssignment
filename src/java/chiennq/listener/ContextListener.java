/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chiennq.listener;

import chiennq.utils.PropertiesFileHelper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Web application lifecycle listener.
 *
 * @author Ezarp
 */
public class ContextListener implements ServletContextListener {
    private final Logger LOGGER = Logger.getLogger(ContextListener.class);
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        configureLog4j(context);
        registerAccessControlConfiguration(context);
//        registerAuthenticationConfiguration(context);
        registerAuthorizationConfiguration(context);
        registerSiteMappingConfiguration(context);
    }
    
    private void configureLog4j(ServletContext context) {
        String log4jRelativeFileLocation = context.getInitParameter("LOG4J_CONFIG_LOCATION");
        String rootPath = context.getRealPath("/");
        String fullPath = rootPath + log4jRelativeFileLocation;
        System.setProperty("PATH", rootPath);
        
        PropertyConfigurator.configure(fullPath);
    }
    
    private void registerAccessControlConfiguration(ServletContext context) {
        String accessControlRelativeFileLocation = context.getInitParameter("ACCESS_CONTROL_PROPERTIES_FILE_LOCATION");
        try {
            Properties accessControlProperties = 
                    PropertiesFileHelper.getProperties(context, accessControlRelativeFileLocation);
            context.setAttribute("ACCESS_CONTROL_LIST", accessControlProperties);
        } catch (IOException ex) {
            LOGGER.error(ex);
        }
    }
    
    private void registerAuthenticationConfiguration(ServletContext context) {
        String authenticationUri = context.getInitParameter("AUTHENTICATION_PROPERTIES_FILE_LOCATION");
        try {
            Properties authenticationProperties = 
                    PropertiesFileHelper.getProperties(context, authenticationUri);
            context.setAttribute("AUTHENTICATION_LIST", authenticationProperties);
        } catch (IOException ex) {
            LOGGER.error(ex);
        }
    }
    
    private void registerAuthorizationConfiguration(ServletContext context) {
        String authorizationUri = context.getInitParameter("AUTHORIZATION_PROPERTIES_FILE_LOCATION");
//        String authorizationUserUri = context.getInitParameter("AUTHORIZATION_USER_PROPERTIES_FILE_LOCATION");
        try {
            Properties authorizationProperties = 
                    PropertiesFileHelper.getProperties(context, authorizationUri);
//            Properties authorizationUserProperties = 
//                    PropertiesFileHelper.getProperties(context, authorizationUserUri);
            context.setAttribute("AUTHORIZATION_LIST", authorizationProperties);
//            context.setAttribute("AUTHORIZATION_USER_LIST", authorizationUserProperties);
        } catch (IOException ex) {
            LOGGER.error(ex);
        }
    }
    
    private void registerSiteMappingConfiguration(ServletContext context) {
        String siteMapUri = context.getInitParameter("SITEMAP_PROPERTIES_FILE_LOCATION");
        try {
            Properties siteMapProperties = 
                PropertiesFileHelper.getProperties(context, siteMapUri);
            context.setAttribute("SITE_MAP", siteMapProperties);
        } catch (IOException ex) {
            LOGGER.error(ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        
    }
}
