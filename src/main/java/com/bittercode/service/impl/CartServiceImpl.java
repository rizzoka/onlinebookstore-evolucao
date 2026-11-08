package com.bittercode.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.bittercode.model.Book;
import com.bittercode.model.Cart;
import com.bittercode.model.ShoppingCart;
import com.bittercode.model.StoreException;
import com.bittercode.service.BookService;
import com.bittercode.service.CartService;

public class CartServiceImpl implements CartService {

    private final BookService bookService;

    public CartServiceImpl() {
        this(new BookServiceImpl());
    }

    // Construtor usado nos testes: permite injetar um BookService falso
    public CartServiceImpl(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public List<Cart> loadCartItems(ShoppingCart cart) throws StoreException {
        List<Cart> items = new ArrayList<Cart>();
        for (String bookId : cart.getBookIds()) {
            // Busca por id usando PreparedStatement. O metodo antigo
            // getBooksByCommaSeperatedBookIds concatenava os ids (vindos do
            // request) direto no SQL, o que permite SQL Injection.
            Book book = bookService.getBookById(bookId);
            if (book != null) {
                items.add(new Cart(book, cart.getQuantity(bookId)));
            }
        }
        return items;
    }
}
