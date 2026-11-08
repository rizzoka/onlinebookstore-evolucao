package com.bittercode.service;

import java.util.List;

import com.bittercode.model.Cart;
import com.bittercode.model.ShoppingCart;
import com.bittercode.model.StoreException;

public interface CartService {

    /**
     * Converte o carrinho da sessao (ids + quantidades) em itens com os dados
     * ATUAIS do livro no banco (nome, autor, preco, estoque).
     * Livros que nao existem mais sao ignorados.
     */
    List<Cart> loadCartItems(ShoppingCart cart) throws StoreException;
}
