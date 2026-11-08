package com.bittercode.service;

import com.bittercode.model.OrderReceipt;
import com.bittercode.model.ShoppingCart;
import com.bittercode.model.StoreException;

public interface OrderService {

    String ERROR_EMPTY_CART = "EMPTY_CART";
    String ERROR_OUT_OF_STOCK = "OUT_OF_STOCK";
    String ERROR_STOCK_UPDATE_FAILED = "STOCK_UPDATE_FAILED";

    /**
     * Finaliza a compra: confere o estoque ATUAL de todos os livros, da baixa
     * nas quantidades e devolve o comprovante.
     *
     * Regra "tudo ou nada": se qualquer livro nao tiver estoque suficiente,
     * nenhuma quantidade e alterada e uma StoreException e lancada.
     *
     * @throws StoreException ERROR_EMPTY_CART, ERROR_OUT_OF_STOCK ou ERROR_STOCK_UPDATE_FAILED
     */
    OrderReceipt placeOrder(ShoppingCart cart) throws StoreException;
}
