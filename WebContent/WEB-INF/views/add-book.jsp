<%@ page isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Book Store - Adicionar Livro</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <nav class="navbar navbar-expand-sm bg-dark navbar-dark">
        <a class="navbar-brand" href="storebooks">Book Store (Seller)</a>
        <ul class="navbar-nav ml-auto">
            <li class="nav-item"><a class="nav-link" href="storebooks">Store Books</a></li>
            <li class="nav-item active"><a class="nav-link" href="addbook">Add Books</a></li>
            <li class="nav-item"><a class="nav-link" href="removebook">Remove Books</a></li>
            <li class="nav-item"><a class="nav-link" href="logout">Logout</a></li>
        </ul>
    </nav>

    <div class="container my-4" style="max-width: 500px;">
        <h3 class="mb-3">Adicionar Livro</h3>

        <c:if test="${not empty successMessage}">
            <div class="alert alert-success"><c:out value="${successMessage}" /></div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger"><c:out value="${errorMessage}" /></div>
        </c:if>

        <form action="addbook" method="post" class="card p-4 bg-white">
            <div class="form-group mb-3">
                <label for="bookName">Nome do Livro</label>
                <input type="text" name="name" id="bookName" class="form-control" placeholder="Digite o nome do livro" required />
            </div>
            <div class="form-group mb-3">
                <label for="bookAuthor">Autor</label>
                <input type="text" name="author" id="bookAuthor" class="form-control" placeholder="Digite o nome do autor" required />
            </div>
            <div class="form-group mb-3">
                <label for="bookPrice">Preço</label>
                <input type="number" name="price" id="bookPrice" class="form-control" step="0.01" min="0.01" placeholder="Digite o preço" required />
            </div>
            <div class="form-group mb-3">
                <label for="bookQuantity">Quantidade</label>
                <input type="number" name="quantity" id="bookQuantity" class="form-control" min="0" placeholder="Digite a quantidade" required />
            </div>
            <button type="submit" class="btn btn-success w-100">Adicionar Livro</button>
        </form>
    </div>
</body>
</html>