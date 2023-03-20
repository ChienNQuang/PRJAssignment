<%-- 
    Document   : searchAsUser
    Created on : Mar 15, 2023, 10:31:02 AM
    Author     : Ezarp
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search</title>
    </head>
    <body>
        <font color="red">
            Welcome, ${sessionScope.USER.fullName}
        </font><br/>
        <a href="shoppingView">Shopping page</a>
        <form action="logoutAction" method="POST">
            <input type="submit" value="Sign Out" name="btAction" />
        </form>
        <h1>Search Page</h1>
        <c:set var="searchValue" value="${param.txtSearchValue}" />
        <form action="searchAction">
            Search text<input type="text" name="txtSearchValue" 
                              value="${searchValue}" /><br/>
            <input type="submit" value="Search" name="btAction" />
        </form>
        <c:if test="${not empty searchValue}" >
            <%-- get page information --%>
            <c:set var="page" value="${requestScope.SEARCH_RESULT}"/>
            <c:set var="accounts" value="${page.content}" />
            <c:set var="pageCount" value="${page.numberOfPages}" />
            
            <%-- set page number --%>
            <c:if test="${not empty param.pageNumber}" >
                <c:set var="pageNumber" value="${param.pageNumber}" />
            </c:if>
            <c:if test="${empty param.pageNumber}" >
                <c:set var="pageNumber" value="${1}" />
            </c:if>
            
            <%-- set size number --%>
            <c:if test="${empty param.sizeNumber}" >
                <c:set var="pageSize" value="${5}" />
            </c:if>
            <c:if test="${not empty param.sizeNumber}" >
                <c:set var="pageSize" value="${param.sizeNumber}" />
            </c:if>
            
            <c:if test="${not empty accounts}" >
                <%-- generate page indexes --%>
                Page: 
                <c:forEach var="pageIndex" begin="1" end="${pageCount}" varStatus="counter" >
                    <c:url var="pageUrl" value="searchAction" >
                        <c:param name="pageNumber" value="${counter.count}" />
                        <c:param name="sizeNumber" value="${pageSize}" />
                        <c:param name="txtSearchValue" value="${searchValue}" />
                    </c:url>
                    <c:if test="${pageNumber == counter.count}" >
                        ${counter.count}
                    </c:if>
                    <c:if test="${pageNumber != counter.count}" >
                        <a href="${pageUrl}" >${counter.count}</a>
                    </c:if>
                    |
                </c:forEach>
                <table border="1">
                    <thead>
                        <tr>
                            <th>No.</th>
                            <th>Username</th>
                            <th>Full Name</th>
                            <th>Role</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="dto" items="${accounts}" varStatus="counter" >
                            <tr>
                                <td>${counter.count + (pageNumber - 1) * pageSize}.</td>
                                <td>
                                    <input type="hidden" name="txtUsername" 
                                           value="" />
                                    ${dto.username}
                                </td>
                                <td>
                                    ${dto.fullName}
                                </td>
                                <td>
                                    <c:if test="${dto.role}">
                                        admin
                                    </c:if>
                                    <c:if test="${!dto.role}" >
                                        user
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
            <c:if test="${empty accounts}" >
                <h2>
                    No record is matched!!
                </h2>
            </c:if>
        </c:if>
    </body>
</html>
