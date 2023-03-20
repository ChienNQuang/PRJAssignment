<%-- 
    Document   : shopping
    Created on : Mar 15, 2023, 2:40:50 PM
    Author     : Ezarp
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Book Store</title>
    </head>
    <body>
        <a href="home" >Back to home page</a>
        <h1>Book Store</h1>
        <c:set var="productsPage" value="${requestScope.PRODUCTS_PAGE}" />
        <c:if test="${not empty productsPage.content}" >
            <%-- get page information --%>
            Choose your favorite book<br/>
            <c:set var="products" value="${productsPage.content}" />
            <c:set var="pageCount" value="${productsPage.numberOfPages}" />
            
            <c:if test="${not empty param.pageNumber}" >
                <c:set var="pageNumber" value="${param.pageNumber}" />
            </c:if>
            <c:if test="${empty param.pageNumber}" >
                <c:set var="pageNumber" value="${1}" />
            </c:if>
            
            <%-- set size number --%>
            <c:if test="${empty param.sizeNumber}" >
                <c:set var="pageSize" value="${10}" />
            </c:if>
            <c:if test="${not empty param.sizeNumber}" >
                <c:set var="pageSize" value="${param.sizeNumber}" />
            </c:if>
            <c:set var="addItemErrors" value="${requestScope.CART_ADD_ITEM_ERRORS}" />
            <c:if test="${not empty addItemErrors.addInvalidQuantity}" >
                <font color="red">
                    ${addItemErrors.addInvalidQuantity}
                </font>
            </c:if>
            <table border="1">
                <thead>
                    <tr>
                        <th>No.</th>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Price</th>
                        <th>Quantity</th>
                        <th>Available</th>
                        <th>Add To Cart</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${products}" varStatus="counter">
                    <form action="cartAddItem" method="POST">
                        <tr>
                            <td style="text-align: right">
                                ${counter.count + pageSize * (pageNumber - 1)}
                            </td>
                            <td>
                                <input type="hidden" name="dllBook" value="${item.sku}" />
                                ${item.name}
                            </td>
                            <td>
                                ${item.description}
                            </td>
                            <td>
                                <div name="price" 
                                     style="text-align: right">${item.price}</div>
                            </td>
                            <td>
                                <input type="number" name="txtQuantity" value="0" 
                                       style="text-align:right; width: 70px;" 
                                       min="0"
                                       max="${item.quantity}"
                                       />
                            </td>
                            <td style="text-align: right">
                                ${item.quantity}
                            </td>
                            <td>
                                <input type="hidden" name="pageNumber" value="${pageNumber}" />
                                <input type="submit" value="AddToCart" name="btAction" />
                            </td>
                        </tr>
                    </form>
                </c:forEach>
                </tbody>
                Page: 
                    <c:forEach var="pageIndex" begin="1" end="${pageCount}" varStatus="counter" >
                        <c:url var="pageUrl" value="shoppingView" >
                            <c:param name="pageNumber" value="${counter.count}" />
                            <c:param name="sizeNumber" value="${pageSize}" />
                        </c:url>
                        <c:if test="${pageNumber == counter.count}" >
                            ${counter.count}
                        </c:if>
                        <c:if test="${pageNumber != counter.count}" >
                            <a href="${pageUrl}" >${counter.count}</a>
                        </c:if>
                        |
                    </c:forEach>
            </table>
            <form action="cartPage">
                <input type="submit" value="View Your Cart" />
            </form>
        </c:if>
    </body>
</html>
