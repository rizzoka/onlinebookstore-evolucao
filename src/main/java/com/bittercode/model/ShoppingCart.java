package com.bittercode.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Carrinho de compras de um cliente: guarda apenas "codigo do livro -> quantidade".
 *
 * Substitui a representacao antiga na sessao (uma String com ids separados por
 * virgula + um atributo "qty_<id>" para cada livro), que tinha bugs: o teste
 * items.contains(id) e o items.replace(id, "") funcionam por substring, entao
 * um id que fosse parte de outro id podia ser ignorado ou apagar o item errado.
 *
 * Esta classe nao depende de HttpServletRequest nem de banco de dados, por isso
 * e facil de testar com JUnit.
 */
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    // LinkedHashMap mantem a ordem em que os livros foram adicionados
    private final Map<String, Integer> quantities = new LinkedHashMap<String, Integer>();

    /** Adiciona 1 unidade do livro (cria o item se ainda nao existir). */
    public void addItem(String bookId) {
        requireValidId(bookId);
        quantities.put(bookId, getQuantity(bookId) + 1);
    }

    /** Remove 1 unidade do livro; quando chega a zero, o item sai do carrinho. */
    public void removeItem(String bookId) {
        requireValidId(bookId);
        int current = getQuantity(bookId);
        if (current > 1) {
            quantities.put(bookId, current - 1);
        } else {
            quantities.remove(bookId);
        }
    }

    /** Quantidade do livro no carrinho (0 se ele nao estiver la). */
    public int getQuantity(String bookId) {
        Integer quantity = quantities.get(bookId);
        return quantity == null ? 0 : quantity;
    }

    /** Codigos dos livros, na ordem de insercao. A colecao devolvida nao pode ser alterada. */
    public Set<String> getBookIds() {
        return Collections.unmodifiableSet(new LinkedHashSet<String>(quantities.keySet()));
    }

    public boolean isEmpty() {
        return quantities.isEmpty();
    }

    /** Quantidade de livros diferentes no carrinho. */
    public int size() {
        return quantities.size();
    }

    public void clear() {
        quantities.clear();
    }

    private static void requireValidId(String bookId) {
        if (bookId == null || bookId.trim().isEmpty()) {
            throw new IllegalArgumentException("bookId must not be null or blank");
        }
    }
}
