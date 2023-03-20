<%-- 
    Document   : viewCart
    Created on : Mar 15, 2023, 5:56:09 PM
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
        <c:set var="items" value="${sessionScope.CART.items}" />
        <c:if test="${not empty items}" >
            <c:set var="checkoutErrors" value="${requestScope.CHECKOUT_ERRORS}" />
            <c:if test="${not empty checkoutErrors.invalidQuantityError}" >
                <font color="red">
                    ${checkoutErrors.invalidQuantityError}
                </font><br/>
            </c:if>
            <table border="1">
                <thead>
                    <tr>
                        <th>No.</th>
                        <th>Name</th>
                        <th>Quantity</th>
                        <th>Total</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <form action="cartRemoveItemAction" method="POST">
                        <c:set var="total" value="${0}" />
                        <c:forEach var="item" items="${items}" varStatus="counter">
                            <tr>
                                <td style="text-align: right">${counter.count}.</td>
                                <td>
                                    ${item.value.name}
                                </td>
                                <td style="text-align: right">
                                    ${item.value.quantity}
                                </td>
                                <td style="text-align: right">
                                    <c:set var="productTotal" value="${item.value.quantity * item.value.price}" />
                                    ${productTotal}
                                    <c:set var="total" value="${total + productTotal}" />
                                </td>
                                <td>
                                    <input type="checkbox" name="chkItem" value="${item.key}" />
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td colspan="3">
                                Total
                            </td>
                            <td>
                                ${total}
                            </td>
                        </tr>
                        <tr>
                            <td colspan="4">
                                <a href="shoppingView">Add more books to your cart</a>
                            </td>
                            <td>
                                <input type="submit" value="Remove Selected Items" name="btAction" />
                            </td>
                        </tr>
                    </form>
                </tbody>
            </table>   
            <form action="cartCheckoutAction" method="POST">
                Name* <input type="text" name="txtName" value="" /><br/>
                <c:if test="${not empty checkoutErrors.nameLengthError}" >
                    <font color="red">
                        ${checkoutErrors.nameLengthError}
                    </font><br/>
                </c:if>
                Address* <textarea name="txtAddress" value="" 
                                   rows="5" cols="20"
                                   style="overflow-y: scroll; resize: none"></textarea><br/>
                <c:if test="${not empty checkoutErrors.addressLengthError}" >
                    <font color="red">
                        ${checkoutErrors.addressLengthError}
                    </font><br/>
                </c:if>
                <input type="submit" value="Checkout" name="btAction" />
            </form>
        </c:if>
        <c:if test="${empty items}" >
            <h2>
                No carts exist!
            </h2>
            <a href="shoppingView">Back to the store</a>
        </c:if>
    </body>
</html>
