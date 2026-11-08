package com.bittercode.service;

import java.util.List;

import com.bittercode.model.Cart;

/**
 * Calculos do carrinho, sem nenhuma dependencia de servlet, sessao ou banco.
 *
 * Antes: o total era calculado dentro do CartServlet, no meio do laco que
 * tambem escrevia o HTML (amountToPay += qty * price).
 * Depois: funcao pura, testavel com JUnit sem subir o Tomcat.
 */
public final class CartCalculator {

    private CartCalculator() {
        // classe utilitaria: nao deve ser instanciada
    }

    /** Valor de uma linha do carrinho: preco do livro x quantidade. */
    public static double calculateLineAmount(Cart item) {
        return item.getBook().getPrice() * item.getQuantity();
    }

    /** Soma de todas as linhas. Lista nula ou vazia resulta em 0. */
    public static double calculateTotal(List<Cart> items) {
        double total = 0;
        if (items == null) {
            return total;
        }
        for (Cart item : items) {
            total += calculateLineAmount(item);
        }
        return total;
    }
}
