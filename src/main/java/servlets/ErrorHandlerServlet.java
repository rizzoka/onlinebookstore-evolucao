package servlets;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bittercode.constant.ResponseCode;
import com.bittercode.constant.Views;
import com.bittercode.model.StoreException;
import com.bittercode.model.UserRole;
import com.bittercode.util.StoreUtil;

/**
 * Trata todos os erros da aplicacao (/error).
 *
 * <p>O web.xml encaminha para ca os erros 404 e 405 e qualquer excecao nao
 * tratada. O servlet apenas coordena: descobre o codigo e a mensagem do erro,
 * registra no log, escolhe o menu e encaminha para a JSP. Todo o HTML fica em
 * {@code error.jsp}.</p>
 *
 * <p>Sobrescreve {@code service()} (e nao {@code doGet()}) de proposito: o
 * container encaminha o erro com o mesmo metodo HTTP da requisicao original
 * (GET, POST...). Com {@code doGet()}, um erro em um POST geraria um novo
 * erro 405 dentro da propria pagina de erro.</p>
 */
public class ErrorHandlerServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ErrorHandlerServlet.class.getName());

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Dados que o container coloca na requisicao ao encaminhar um erro
        Throwable throwable = (Throwable) req.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        Integer statusAttr = (Integer) req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String servletName = (String) req.getAttribute(RequestDispatcher.ERROR_SERVLET_NAME);
        String requestUri = (String) req.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        int statusCode = (statusAttr == null) ? 0 : statusAttr;
        String errorCode = ResponseCode.INTERNAL_SERVER_ERROR.name();
        String errorMessage = ResponseCode.INTERNAL_SERVER_ERROR.getMessage();

        Optional<ResponseCode> responseCode = ResponseCode.getMessageByStatusCode(statusCode);
        if (responseCode.isPresent()) {
            errorCode = responseCode.get().name();
            errorMessage = responseCode.get().getMessage();
        }

        // Erros da propria loja trazem codigo e mensagem mais especificos
        if (throwable instanceof StoreException) {
            StoreException storeException = (StoreException) throwable;
            statusCode = storeException.getStatusCode();
            errorCode = storeException.getErrorCode();
            errorMessage = storeException.getMessage();
        }

        logError(statusCode, errorCode, errorMessage, servletName, requestUri, throwable);

        req.setAttribute("errorCode", errorCode);
        req.setAttribute("errorMessage", errorMessage);
        defineMenu(req);

        RequestDispatcher rd = req.getRequestDispatcher(Views.ERROR);
        if (res.isCommitted()) {
            // Parte da pagina ja foi enviada ao navegador: nao da para trocar
            // a resposta (forward), so acrescentar o erro no fim (include)
            rd.include(req, res);
        } else {
            rd.forward(req, res);
        }
    }

    /** Escolhe o menu de acordo com quem esta logado; a aba Home fica ativa. */
    private void defineMenu(HttpServletRequest req) {
        HttpSession session = req.getSession();
        if (StoreUtil.isLoggedIn(UserRole.CUSTOMER, session)) {
            req.setAttribute("navType", "customer");
        } else if (StoreUtil.isLoggedIn(UserRole.SELLER, session)) {
            req.setAttribute("navType", "seller");
        } else {
            req.setAttribute("navType", "public");
        }
        req.setAttribute("activeTab", "home");
    }

    /** Registra o erro no log do servidor (com stack trace quando houver excecao). */
    private void logError(int statusCode, String errorCode, String errorMessage,
            String servletName, String requestUri, Throwable throwable) {
        String message = String.format("Erro %d (%s) em %s [servlet: %s]: %s",
                statusCode, errorCode, requestUri, servletName, errorMessage);
        if (throwable != null) {
            LOGGER.log(Level.SEVERE, message, throwable);
        } else {
            LOGGER.warning(message);
        }
    }
}
