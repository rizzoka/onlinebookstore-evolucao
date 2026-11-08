package com.bittercode.constant;

/**
 * Nomes dos atributos guardados na HttpSession.
 *
 * Antes: o carrinho era espalhado em varias chaves soltas ("items", "qty_<id>",
 * "cartItems", "amountToPay", "selectedBookId"), conhecidas por varios servlets.
 * Depois: existe uma unica chave, que guarda um objeto ShoppingCart.
 */
public final class SessionAttributes {

    public static final String SHOPPING_CART = "shoppingCart";

    private SessionAttributes() {
        // classe utilitaria: nao deve ser instanciada
    }
}
