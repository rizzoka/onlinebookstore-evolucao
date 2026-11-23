<%@ page isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- Pagina "Sobre". Preparada pelo AboutServlet, que define:
       navType   -> "customer" ou "seller" (qual menu exibir)
       activeTab -> "about" (aba destacada no menu) --%>
<jsp:include page="/WEB-INF/views/fragments/header-${navType}.jsp" />

	<iframe src="https://flowcv.me/shashirajraja" class="holds-the-iframe"
		title="My Personal Website" width="100%" height="100%"></iframe>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
