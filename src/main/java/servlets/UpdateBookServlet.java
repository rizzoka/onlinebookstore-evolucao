package servlets;

import java.io.IOException;

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

public class UpdateBookServlet extends HttpServlet {

    private final BookService bookService = new BookServiceImpl();
    private final BookValidationService validationService = new BookValidationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        if (!StoreUtil.isLoggedIn(UserRole.SELLER, req.getSession())) {
            res.sendRedirect("SellerLogin.html");
            return;
        }

        loadBookForEdit(req);
        req.getRequestDispatcher(Views.UPDATE_BOOK).forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        if (!StoreUtil.isLoggedIn(UserRole.SELLER, req.getSession())) {
            res.sendRedirect("SellerLogin.html");
            return;
        }

        if (req.getParameter("updateFormSubmitted") != null) {
            processBookUpdate(req);
        } else {
            loadBookForEdit(req);
        }

        req.getRequestDispatcher(Views.UPDATE_BOOK).forward(req, res);
    }

    private void loadBookForEdit(HttpServletRequest req) {
        String bookId = req.getParameter("bookId");
        if (bookId == null || bookId.trim().isEmpty()) {
            req.setAttribute("errorMessage", "Código do livro não informado.");
            return;
        }

        try {
            Book book = bookService.getBookById(bookId.trim());
            if (book == null) {
                req.setAttribute("errorMessage", "Livro não encontrado.");
            } else {
                req.setAttribute("book", book);
            }
        } catch (StoreException e) {
            req.setAttribute("errorMessage", e.getErrorMessage());
        }
    }

    private void processBookUpdate(HttpServletRequest req) {
        try {
            Book book = extractBookFromRequest(req);
            req.setAttribute("book", book);
            validationService.validate(book);

            String message = bookService.updateBook(book);
            if (ResponseCode.SUCCESS.name().equalsIgnoreCase(message)) {
                req.setAttribute("successMessage", "Dados do livro atualizados com sucesso!");
            } else {
                req.setAttribute("errorMessage", "Falha ao atualizar o livro.");
            }
        } catch (StoreException e) {
            req.setAttribute("errorMessage", e.getErrorMessage());
        }
    }

    private Book extractBookFromRequest(HttpServletRequest req) throws StoreException {
        String barcode = req.getParameter(BooksDBConstants.COLUMN_BARCODE);
        String name = req.getParameter(BooksDBConstants.COLUMN_NAME);
        String author = req.getParameter(BooksDBConstants.COLUMN_AUTHOR);
        String priceStr = req.getParameter(BooksDBConstants.COLUMN_PRICE);
        String qtyStr = req.getParameter(BooksDBConstants.COLUMN_QUANTITY);

        try {
            double price = Double.parseDouble(priceStr);
            int quantity = Integer.parseInt(qtyStr);
            return new Book(barcode, name, author, price, quantity);
        } catch (NumberFormatException | NullPointerException e) {
            throw new StoreException(ResponseCode.BAD_REQUEST.name(),
                    "Preço e quantidade devem ser números válidos.");
        }
    }
}