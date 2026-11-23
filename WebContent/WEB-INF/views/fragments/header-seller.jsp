<%@ page isELIgnored="false" pageEncoding="UTF-8" %>
<%-- Abertura da pagina + menu do VENDEDOR (copiado de SellerHome.html).
     A aba ativa vem do atributo de requisicao "activeTab" (sem <script>). --%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/fragments/head.jspf" %>
<%-- Bootstrap 3 + jQuery mantidos como no SellerHome.html (unificacao do
     Bootstrap fica para o plano de evolucao). --%>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
</head>
<body>
	<header>
		<nav class="navbar navbar-expand-sm bg-dark"
			style="margin-bottom: 0px">
			<a class="navbar-brand">
				<img src="logo.png" alt="Google logo" width="60" height="30px">
			</a>

			<button style="background-color: white;" class="navbar-toggler"
				type="button" data-toggle="collapse" data-target="#navbarNav"
				aria-controls="navbarNav" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"
					style="color: #fff; font-size: 28px;"></span>
			</button>

			<div class="collapse navbar-collapse" id="navbarNav">
				<ul class="navbar-nav">
					<li class="nav-item"><span><a
							class="nav-link ${activeTab == 'home' ? 'active' : ''}"
							href="storebooks" id="home">Home</a></span></li>
					<li class="nav-item "><span><a
							class="nav-link ${activeTab == 'storebooks' ? 'active' : ''}"
							href="storebooks" id="storebooks">Store Books</a></span></li>
					<li class="nav-item "><span><a
							class="nav-link ${activeTab == 'addbook' ? 'active' : ''}"
							href="addbook" id="addbook">Add Books</a></span></li>
					<li class="nav-item "><span><a
							class="nav-link ${activeTab == 'removebook' ? 'active' : ''}"
							href="removebook" id="removebook">Remove Books</a></span></li>
					<li class="nav-item "><span><a
							class="nav-link ${activeTab == 'about' ? 'active' : ''}"
							href="about" id="about">About Us</a></span></li>
					<li class="nav-item "><span><a class="nav-link"
							href="logout" id="logout">Logout</a></span></li>
				</ul>
			</div>
		</nav>
	</header>
