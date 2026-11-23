<%@ page isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- Pagina de erro. Preparada pelo ErrorHandlerServlet, que define:
       navType      -> "customer", "seller" ou "public" (qual menu exibir)
       activeTab    -> "home"
       errorCode    -> ex.: PAGE_NOT_FOUND
       errorMessage -> texto exibido ao usuario
     <c:out> escapa o texto (< vira &lt;), evitando XSS. --%>
<jsp:include page="/WEB-INF/views/fragments/header-${navType}.jsp" />

	<div class="container my-5">
		<div class="alert alert-danger" role="alert"
			style="max-width: 450px; text-align: center; margin: auto;">
			<h4 class="alert-heading"><c:out value="${errorCode}" /></h4>
			<hr>
			<p class="mb-0"><c:out value="${errorMessage}" /></p>
		</div>
	</div>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
