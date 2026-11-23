<%@ page isELIgnored="false" pageEncoding="UTF-8" %>
<%-- Abertura da pagina + menu do VISITANTE (copiado de index.html/login.html).
     A aba ativa vem do atributo de requisicao "activeTab" (sem <script>). --%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/fragments/head.jspf" %>
</head>
<body>
	<header>
		<nav class="navbar navbar-expand-sm bg-dark">
			<a class="navbar-brand">
				<img src="logo.png" alt="Google logo" width="60" height="30px">
			</a>

			<button style="background-color: white;" class="navbar-toggler"
				type="button" data-toggle="collapse" data-target="#navbarNav"
				aria-controls="navbarNav" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>

			<div class="collapse navbar-collapse" id="navbarNav">
				<ul class="navbar-nav">
					<li class="nav-item"><span><a
							class="nav-link ${activeTab == 'home' ? 'active' : ''}"
							href="index.html">Home</a></span></li>
					<li class="nav-item "><span><a
							class="nav-link ${activeTab == 'login' ? 'active' : ''}"
							href="login.html">Login</a></span></li>
					<li class="nav-item "><span><a
							class="nav-link ${activeTab == 'register' ? 'active' : ''}"
							href="CustomerRegister.html">Register</a></span></li>
				</ul>
			</div>
		</nav>
	</header>
