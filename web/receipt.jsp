<%-- 
    Document   : receipt
    Created on : Mar 15, 2023, 8:45:13 PM
    Author     : Ezarp
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Receipt</title>
    </head>
    <body>
        <a href="home" >Back to home page</a>
        <c:set var="receipt" value="${requestScope.RECEIPT}" />
        <c:if test="${not empty receipt}">
            <c:set var="order" value="${receipt.order}" />
            <c:set var="details" value="${receipt.productDetail}" />
            <h1>Receipt ID: ${order.id}</h1>
            <h2>Customer name: ${order.name}</h2>
            <h2>Customer address: ${order.address}</h2>
            <h2>Date: ${order.date}</h2>
            <h2>Total: ${order.total}</h2>
            <table border="1">
                <thead>
                    <tr>
                        <th>No.</th>
                        <th>Product SKU</th>
                        <th>Product name</th>
                        <th>Quantity</th>
                        <th>Price</th>
                        <th>Total</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="detail" items="${details}" varStatus="counter">
                        <tr>
                            <td style="text-align: right">${counter.count}.</td>
                            <td style="text-align: right">
                                ${detail.sku}
                            </td>
                            <td>
                                ${detail.name}
                            </td>
                            <td style="text-align: right">
                                ${detail.quantity}
                            </td>
                            <td style="text-align: right">
                                ${detail.price}
                            </td>
                            <td style="text-align: right">
                                ${detail.quantity * detail.price}
                            </td>
                        </tr>
                    </c:forEach>
                    
                </tbody>
            </table>

        </c:if>
        <c:if test="${empty receipt}">
            <h2>
                This order does not exist!
            </h2>
        </c:if>
        <a href="shoppingView" >Click here to continue shopping</a>
    </body>
</html>
