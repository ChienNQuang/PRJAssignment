<%-- 
    Document   : login
    Created on : Mar 14, 2023, 4:59:16 PM
    Author     : Ezarp
--%>

<%@page session="false" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
    </head>
    <body>
        <h1>Login Page</h1>
        <c:set var="errors" value="${requestScope.LOGIN_ERRORS}" />
        <form action="loginAction" method="POST">
            <c:if test="${not empty errors.wrongUsernameOrPasswordError}" >
                <font color="red">
                    ${errors.wrongUsernameOrPasswordError}
                </font><br/>
            </c:if>
            Username: <input type="text" name="txtUsername" value="${param.username}" /><br/>
            <c:if test="${not empty errors.emptyUsernameError}" >
                <font color="red">
                    ${errors.emptyUsernameError}
                </font><br/>
            </c:if>
            Password: <input type="password" name="txtPassword" value="" /><br/>
            <c:if test="${not empty errors.emptyPasswordError}" >
                <font color="red">
                    ${errors.emptyPasswordError}
                </font><br/>
            </c:if>
            <input type="submit" value="Login" name="btAction" />
            <input type="reset" value="Reset" />
        </form>
        
        <a href="registerPage">Click here to create new account</a>
        <a href="shoppingView">Buy Books</a>
    </body>
</html>
