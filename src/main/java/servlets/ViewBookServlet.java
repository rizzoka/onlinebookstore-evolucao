package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bittercode.constant.Views;
import com.bittercode.model.Book;
import com.bittercode.model.StoreException;
import com.bittercode.model.UserRole;
import com.bittercode.service.BookService;
import com.bittercode.service.impl.BookServiceImpl;
import com.bittercode.util.StoreUtil;

public class ViewBookServlet extends HttpServlet {

    private final BookService bookService = new BookServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        processRequest(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        if (!StoreUtil.isLoggedIn(UserRole.CUSTOMER, req.getSession())) {
            res.sendRedirect("CustomerLogin.html");
            return;
        }

        try {
            StoreUtil.updateCartItems(req);
            List<Book> books = bookService.getAllBooks();
            Map<String, Integer> cartQuantities = buildCartQuantitiesMap(req.getSession(), books);

            req.setAttribute("books", books);
            req.setAttribute("cartQuantities", cartQuantities);
        } catch (StoreException e) {
            req.setAttribute("errorMessage", e.getErrorMessage());
        }

        req.getRequestDispatcher(Views.VIEW_BOOKS).forward(req, res);
    }

    private Map<String, Integer> buildCartQuantitiesMap(HttpSession session, List<Book> books) {
        Map<String, Integer> quantities = new HashMap<>();
        for (Book book : books) {
            Object qtyObj = session.getAttribute("qty_" + book.getBarcode());
            int qty = (qtyObj instanceof Integer) ? (Integer) qtyObj : 0;
            quantities.put(book.getBarcode(), qty);
        }
        return quantities;
    }
}