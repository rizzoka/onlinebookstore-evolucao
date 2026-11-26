package com.bittercode.constant;

public final class Views {

    private Views() {    }

    private static final String ROOT = "/WEB-INF/views/";

    // Módulo de catálogo de livros
    public static final String VIEW_BOOKS  = ROOT + "view-books.jsp";
    public static final String ADD_BOOK    = ROOT + "add-book.jsp";
    public static final String REMOVE_BOOK = ROOT + "remove-book.jsp";
    public static final String UPDATE_BOOK = ROOT + "update-book.jsp";
    public static final String STORE_BOOKS = ROOT + "store-books.jsp";
}
