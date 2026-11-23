package com.bittercode.constant;

/**
 * Caminhos das páginas JSP usadas pelos servlets em
 * {@code request.getRequestDispatcher(...).forward(...)}.
 *
 * <p>As JSPs ficam em {@code /WEB-INF/views}, por isso não podem ser abertas
 * direto pela URL: só são exibidas depois que um servlet prepara os dados.</p>
 *
 * <p>Classe final com construtor privado (e não uma interface de constantes,
 * como {@link BookStoreConstants}), para que ninguém a implemente ou instancie.</p>
 */
public final class Views {

	private static final String ROOT = "/WEB-INF/views/";

	/** Página "Sobre" (usada por {@code AboutServlet}). */
	public static final String ABOUT = ROOT + "about.jsp";

	/** Página de erro (usada por {@code ErrorHandlerServlet}). */
	public static final String ERROR = ROOT + "error.jsp";

	/** Aviso "faça login para continuar", para visitantes não logados. */
	public static final String LOGIN_REQUIRED = ROOT + "login-required.jsp";

	private Views() {
		// classe utilitária: não deve ser instanciada
	}
}
