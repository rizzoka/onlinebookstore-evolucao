package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bittercode.constant.ResponseCode;
import com.bittercode.constant.Views;
import com.bittercode.model.StoreException;
import com.bittercode.model.UserRole;
import com.bittercode.service.BookService;
import com.bittercode.service.impl.BookServiceImpl;
import com.bittercode.util.StoreUtil;

public class RemoveBookServlet extends HttpServlet {

    private final BookService bookService = new BookServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        if (!StoreUtil.isLoggedIn(UserRole.SELLER, req.getSession())) {
            res.sendRedirect("SellerLogin.html");
            return;
        }

        req.getRequestDispatcher(Views.REMOVE_BOOK).forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        if (!StoreUtil.isLoggedIn(UserRole.SELLER, req.getSession())) {
            res.sendRedirect("SellerLogin.html");
            return;
        }

        String bookId = req.getParameter("bookId");
        if (bookId == null || bookId.trim().isEmpty()) {
            req.setAttribute("errorMessage", "Informe o código do livro para remover.");
            req.getRequestDispatcher(Views.REMOVE_BOOK).forward(req, res);
            return;
        }

        try {
            String responseCode = bookService.deleteBookById(bookId.trim());
            if (ResponseCode.SUCCESS.name().equalsIgnoreCase(responseCode)) {
                req.setAttribute("successMessage", "Livro removido com sucesso!");
            } else {
                req.setAttribute("errorMessage", "Livro não encontrado na loja.");
            }
        } catch (StoreException e) {
            req.setAttribute("errorMessage", e.getErrorMessage());
        }

        req.getRequestDispatcher(Views.REMOVE_BOOK).forward(req, res);
    }
}