package com.bittercode.service;

import com.bittercode.constant.ResponseCode;
import com.bittercode.model.Book;
import com.bittercode.model.StoreException;

public class BookValidationService {

    public void validate(Book book) throws StoreException {
        if (book.getName() == null || book.getName().trim().isEmpty()) {
            throw new StoreException(ResponseCode.BAD_REQUEST.name(), "Nome do livro é obrigatório");
        }
        if (book.getPrice() <= 0) {
            throw new StoreException(ResponseCode.BAD_REQUEST.name(), "Preço deve ser positivo");
        }
        if (book.getQuantity() < 0) {
            throw new StoreException(ResponseCode.BAD_REQUEST.name(), "Quantidade não pode ser negativa");
        }
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new StoreException(ResponseCode.BAD_REQUEST.name(), "Autor é obrigatório");
        }
    }
}
