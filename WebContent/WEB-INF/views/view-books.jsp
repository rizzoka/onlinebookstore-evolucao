<%@ page isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Book Store - Available Books</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <nav class="navbar navbar-expand-sm bg-dark navbar-dark">
        <a class="navbar-brand" href="viewbook">Book Store</a>
        <ul class="navbar-nav ml-auto">
            <li class="nav-item active"><a class="nav-link" href="viewbook">Available Books</a></li>
            <li class="nav-item"><a class="nav-link" href="cart">Cart</a></li>
            <li class="nav-item"><a class="nav-link" href="about">About Us</a></li>
            <li class="nav-item"><a class="nav-link" href="logout">Logout</a></li>
        </ul>
    </nav>

    <div class="container my-4">
        <div class="d-flex justify-content-between align-items-center mb-3 p-2 bg-secondary text-white rounded">
            <h4 class="mb-0">Available Books</h4>
            <form action="cart" method="post" class="mb-0">
                <input type="submit" class="btn btn-primary" name="cart" value="Proceed" />
            </form>
        </div>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger"><c:out value="${errorMessage}" /></div>
        </c:if>

        <div class="card-columns">
            <c:forEach var="book" items="${books}">
                <c:set var="cartQty" value="${cartQuantities[book.barcode]}" />
                <div class="card">
                    <div class="row card-body">
                        <img class="col-sm-6" src="logo.png" alt="Book cover">
                        <div class="col-sm-6">
                            <h5 class="card-title text-success"><c:out value="${book.name}" /></h5>
                            <p class="card-text">
                                Author: <span class="text-primary font-weight-bold"><c:out value="${book.author}" /></span>
                            </p>
                        </div>
                    </div>
                    <div class="row card-body">
                        <div class="col-sm-6">
                            <p class="card-text">
                                <span>Id: <c:out value="${book.barcode}" /></span><br>
                                <c:choose>
                                    <c:when test="${book.quantity < 20}">
                                        <span class="text-danger">Only ${book.quantity} items left</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-success">Trending</span>
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                        <div class="col-sm-6">
                            <p class="card-text">
                                Price: <span style="font-weight:bold; color:green;">&#8377; ${book.price}</span>
                            </p>
                            <c:choose>
                                <c:when test="${book.quantity > 0}">
                                    <c:choose>
                                        <c:when test="${cartQty == 0}">
                                            <form action="viewbook" method="post">
                                                <input type="hidden" name="selectedBookId" value="<c:out value='${book.barcode}' />" />
                                                <input type="hidden" name="qty_${book.barcode}" value="1" />
                                                <input type="submit" class="btn btn-primary" name="addToCart" value="Add To Cart" />
                                            </form>
                                        </c:when>
                                        <c:otherwise>
                                            <form method="post" action="cart">
                                                <button type="submit" name="removeFromCart" class="btn btn-danger btn-sm">-</button>
                                                <input type="hidden" name="selectedBookId" value="<c:out value='${book.barcode}' />" />
                                                <span class="mx-1">${cartQty}</span>
                                                <button type="submit" name="addToCart" class="btn btn-success btn-sm">+</button>
                                            </form>
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <p class="btn btn-danger">Out Of Stock</p>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <div class="mt-3">
            <form action="cart" method="post">
                <input type="submit" class="btn btn-success" name="cart" value="Proceed to Checkout" />
            </form>
        </div>
    </div>
</body>
</html>