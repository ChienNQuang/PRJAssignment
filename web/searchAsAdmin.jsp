<%-- 
    Document   : searchAsAdmin
    Created on : Mar 14, 2023, 9:14:35 PM
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
        </font>
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
            
            <c:set var="deleteErrors" value="${requestScope.DELETE_ERRORS}" />
            <c:if test="${not empty deleteErrors.deleteSelfError}" >
                <font color="red" >
                    ${deleteErrors.deleteSelfError}
                </font>
            </c:if>
            <c:if test="${not empty accounts}" >
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
                            <th>Full name</th>
                            <th>Role</th>
                            <th>Delete</th>
                            <th>Update</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="dto" items="${accounts}" varStatus="counter" >
                            <form action="updateAccountAction" method="POST">
                                <tr>
                                    <td>${counter.count + (pageNumber - 1) * pageSize}.</td>
                                    <td>
                                        <input type="hidden" name="txtUsername" 
                                               value="${dto.username}" />
                                        ${dto.username}
                                        <%--<c:set var="username" value="${dto.username}" scope="request" ></c:set>--%>
                                    </td>
                                    <td>
                                        ${dto.fullName}
                                    </td>
                                    <td>
                                        <input type="checkbox" name="chkRole" value="ON" 
                                               <c:if test="${dto.role}">
                                                   checked="checked"
                                               </c:if>
                                               />
                                    </td>
                                    <td>
                                        <c:url var="deleteUrl" value="deleteAccountAction">
                                            <c:param name="pk" value="${dto.username}" />
                                            <c:param name="lastSearchValue" value="${searchValue}" />
                                            <c:param name="lastPageNumber" value="${pageNumber}" />
                                        </c:url>
                                        <a href="<c:out value="${deleteUrl}" default="DistpatchServlet" />">
                                            Delete
                                        </a>
                                    </td>
                                    <td>
                                        <input type="hidden" name="lastPageNumber" value="${pageNumber}" />
                                        <input type="hidden" name="lastSearchValue" value="${searchValue}" />
                                        <input type="submit" value="Update" name="btAction" />
                                    </td>
                                </tr>
                            </form>
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
