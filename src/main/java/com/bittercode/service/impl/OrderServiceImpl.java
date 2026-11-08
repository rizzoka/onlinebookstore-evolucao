package com.bittercode.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.bittercode.constant.ResponseCode;
import com.bittercode.model.Book;
import com.bittercode.model.Cart;
import com.bittercode.model.OrderReceipt;
import com.bittercode.model.ShoppingCart;
import com.bittercode.model.StoreException;
import com.bittercode.service.BookService;
import com.bittercode.service.CartCalculator;
import com.bittercode.service.CartService;
import com.bittercode.service.OrderService;

/**
 * Regras de negocio do checkout, antes espalhadas no ProcessPaymentServlet
 * (e duplicadas no ReceiptServlet).
 */
public class OrderServiceImpl implements OrderService {

    // Protege o "ler estoque -> conferir -> gravar estoque" contra duas compras
    // simultaneas dentro da mesma aplicacao. Para varios servidores, o correto
    // seria uma transacao no banco (ver plano de evolucao).
    private static final Object STOCK_LOCK = new Object();

    private final BookService bookService;
    private final CartService cartService;

    public OrderServiceImpl() {
        BookService books = new BookServiceImpl();
        this.bookService = books;
        this.cartService = new CartServiceImpl(books);
    }

    // Construtor usado nos testes
    public OrderServiceImpl(BookService bookService, CartService cartService) {
        this.bookService = bookService;
        this.cartService = cartService;
    }

    @Override
    public OrderReceipt placeOrder(ShoppingCart cart) throws StoreException {
        synchronized (STOCK_LOCK) {
            List<Cart> items = cartService.loadCartItems(cart);
            if (items.isEmpty()) {
                throw new StoreException(ERROR_EMPTY_CART, "Your cart is empty.");
            }

            ensureStockIsAvailable(items);
            decreaseStock(items);

            return new OrderReceipt(items, CartCalculator.calculateTotal(items));
        }
    }

    /** Valida TODOS os itens antes de alterar qualquer coisa no banco. */
    private void ensureStockIsAvailable(List<Cart> items) throws StoreException {
        StringBuilder problems = new StringBuilder();
        for (Cart item : items) {
            Book book = item.getBook();
            if (item.getQuantity() > book.getQuantity()) {
                if (problems.length() > 0) {
                    problems.append("; ");
                }
                problems.append(book.getName())
                        .append(" (requested ").append(item.getQuantity())
                        .append(", available ").append(book.getQuantity()).append(")");
            }
        }
        if (problems.length() > 0) {
            throw new StoreException(ERROR_OUT_OF_STOCK, "Not enough stock for: " + problems);
        }
    }

    /** Da baixa no estoque; se algum item falhar, desfaz os que ja foram gravados. */
    private void decreaseStock(List<Cart> items) throws StoreException {
        List<Cart> alreadyUpdated = new ArrayList<Cart>();
        try {
            for (Cart item : items) {
                Book book = item.getBook();
                updateStock(book.getBarcode(), book.getQuantity() - item.getQuantity());
                alreadyUpdated.add(item);
            }
        } catch (StoreException e) {
            rollbackStock(alreadyUpdated);
            throw e;
        }
    }

    private void updateStock(String bookId, int newQuantity) throws StoreException {
        String response = bookService.updateBookQtyById(bookId, newQuantity);
        // updateBookQtyById NAO lanca excecao em caso de erro: devolve um texto
        // "FAILURE : ...". O servlet antigo ignorava esse retorno.
        if (!ResponseCode.SUCCESS.name().equals(response)) {
            throw new StoreException(ERROR_STOCK_UPDATE_FAILED,
                    "Could not update the stock of book " + bookId + ": " + response);
        }
    }

    private void rollbackStock(List<Cart> updatedItems) {
        for (Cart item : updatedItems) {
            Book book = item.getBook();
            try {
                updateStock(book.getBarcode(), book.getQuantity()); // volta ao valor original
            } catch (StoreException ignored) {
                // melhor esforco: nao ha o que fazer se o rollback tambem falhar
            }
        }
    }
}
