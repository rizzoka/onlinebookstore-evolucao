<%@ page isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- Pagina exibida quando um visitante nao logado tenta abrir uma pagina
     restrita. Reproduz o conteudo de login.html + a mensagem que os servlets
     imprimiam depois do </html>. O servlet deve definir:
       navType   -> "public"
       activeTab -> "login" --%>
<jsp:include page="/WEB-INF/views/fragments/header-${navType}.jsp" />

	<div id="topmid"><h1>Welcome to Online Book Store</h1></div>
	<br>
	<table class="tab">
		<tr style="background-color: white; color: green; border-radius: 25%">
			<td>Login or SignUp Below</td>
		</tr>
		<tr>
			<td><a href="SellerLogin.html">Login As Admin</a></td>
		</tr>
		<tr>
			<td><a href="CustomerLogin.html"> Login As<span> Customer</span></a></td>
		</tr>
		<tr>
			<td><a href="CustomerRegister.html">New user ! Register Here</a></td>
		</tr>
	</table>
	<table class="tab">
		<tr>
			<td>Please Login First to Continue!!</td>
		</tr>
	</table>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
