package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bittercode.constant.Views;
import com.bittercode.model.UserRole;
import com.bittercode.util.StoreUtil;

/**
 * Exibe a pagina "Sobre" (/about).
 *
 * <p>O servlet apenas coordena: descobre quem esta logado, informa a JSP qual
 * menu e qual aba usar e encaminha (forward). Todo o HTML fica nas JSPs.</p>
 */
public class AboutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();

        if (StoreUtil.isLoggedIn(UserRole.CUSTOMER, session)) {
            showAbout(req, res, "customer");
        } else if (StoreUtil.isLoggedIn(UserRole.SELLER, session)) {
            showAbout(req, res, "seller");
        } else {
            // Visitante nao logado: pede login antes de continuar
            req.setAttribute("navType", "public");
            req.setAttribute("activeTab", "login");
            req.getRequestDispatcher(Views.LOGIN_REQUIRED).forward(req, res);
        }
    }

    private void showAbout(HttpServletRequest req, HttpServletResponse res, String navType)
            throws ServletException, IOException {
        req.setAttribute("navType", navType);
        req.setAttribute("activeTab", "about");
        req.getRequestDispatcher(Views.ABOUT).forward(req, res);
    }
}
