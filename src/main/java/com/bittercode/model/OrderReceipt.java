package com.bittercode.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Resultado de um pedido finalizado: o que foi comprado e o valor total pago.
 * Objeto imutavel, montado pelo OrderService e apenas exibido pelo servlet.
 */
public final class OrderReceipt implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Cart> items;
    private final double totalAmount;

    public OrderReceipt(List<Cart> items, double totalAmount) {
        this.items = Collections.unmodifiableList(new ArrayList<Cart>(items));
        this.totalAmount = totalAmount;
    }

    public List<Cart> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}
