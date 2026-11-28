package servlets;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bittercode.constant.ResponseCode;
import com.bittercode.constant.Views;
import com.bittercode.constant.db.BooksDBConstants;
import com.bittercode.model.Book;
import com.bittercode.model.StoreException;
import com.bittercode.model.UserRole;
import com.bittercode.service.BookService;
import com.bittercode.service.BookValidationService;
import com.bittercode.service.impl.BookServiceImpl;
import com.bittercode.util.StoreUtil;

public class AddBookServlet extends HttpServlet {

    private final BookService bookService = new BookServiceImpl();
    private final BookValidationService validationService = new BookValidationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        if (!StoreUtil.isLoggedIn(UserRole.SELLER, req.getSession())) {
            res.sendRedirect("SellerLogin.html");
            return;
        }

        req.setAttribute("navType", "seller");
        req.setAttribute("activeTab", "addbook");
        req.getRequestDispatcher(Views.ADD_BOOK).forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        if (!StoreUtil.isLoggedIn(UserRole.SELLER, req.getSession())) {
            res.sendRedirect("SellerLogin.html");
            return;
        }

        try {
            Book book = extractBookFromRequest(req);
            validationService.validate(book);

            String responseCode = bookService.addBook(book);
            if (ResponseCode.SUCCESS.name().equalsIgnoreCase(responseCode)) {
                req.setAttribute("successMessage", "Livro adicionado com sucesso!");
            } else {
                req.setAttribute("errorMessage", "Não foi possível adicionar o livro.");
            }
        } catch (StoreException e) {
            req.setAttribute("errorMessage", e.getErrorMessage());
        }

        req.setAttribute("navType", "seller");
        req.setAttribute("activeTab", "addbook");
        req.getRequestDispatcher(Views.ADD_BOOK).forward(req, res);
    }

    private Book extractBookFromRequest(HttpServletRequest req) throws StoreException {
        String name = req.getParameter(BooksDBConstants.COLUMN_NAME);
        String author = req.getParameter(BooksDBConstants.COLUMN_AUTHOR);
        String priceStr = req.getParameter(BooksDBConstants.COLUMN_PRICE);
        String qtyStr = req.getParameter(BooksDBConstants.COLUMN_QUANTITY);

        try {
            double price = Double.parseDouble(priceStr);
            int quantity = Integer.parseInt(qtyStr);
            String barcode = UUID.randomUUID().toString();
            return new Book(barcode, name, author, price, quantity);
        } catch (NumberFormatException | NullPointerException e) {
            throw new StoreException(ResponseCode.BAD_REQUEST.name(),
                    "Preço e quantidade devem ser números válidos.");
        }
    }
}