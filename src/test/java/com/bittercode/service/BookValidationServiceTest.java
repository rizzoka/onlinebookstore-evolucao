package com.bittercode.service;

import com.bittercode.model.Book;
import com.bittercode.model.StoreException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookValidationServiceTest {

    private BookValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new BookValidationService();
    }

    @Test
    void deveValidarLivroValido() {
        Book book = new Book("123", "Clean Code", "Robert C. Martin", 89.90, 10);
        assertDoesNotThrow(() -> validationService.validate(book));
    }

    @Test
    void deveLancarExcecaoParaPrecoZero() {
        Book book = new Book("123", "Clean Code", "Robert C. Martin", 0, 10);
        StoreException ex = assertThrows(StoreException.class, () -> validationService.validate(book));
        assertEquals("Preço deve ser positivo", ex.getErrorMessage());
    }

    @Test
    void deveLancarExcecaoParaPrecoNegativo() {
        Book book = new Book("123", "Clean Code", "Robert C. Martin", -50, 10);
        assertThrows(StoreException.class, () -> validationService.validate(book));
    }

    @Test
    void deveLancarExcecaoParaQuantidadeNegativa() {
        Book book = new Book("123", "Clean Code", "Robert C. Martin", 89.90, -1);
        StoreException ex = assertThrows(StoreException.class, () -> validationService.validate(book));
        assertEquals("Quantidade não pode ser negativa", ex.getErrorMessage());
    }

    @Test
    void deveLancarExcecaoParaNomeVazio() {
        Book book = new Book("123", "", "Robert C. Martin", 89.90, 10);
        assertThrows(StoreException.class, () -> validationService.validate(book));
    }

    @Test
    void deveLancarExcecaoParaNomeNulo() {
        Book book = new Book("123", null, "Robert C. Martin", 89.90, 10);
        assertThrows(StoreException.class, () -> validationService.validate(book));
    }

    @Test
    void deveLancarExcecaoParaAutorVazio() {
        Book book = new Book("123", "Clean Code", "", 89.90, 10);
        assertThrows(StoreException.class, () -> validationService.validate(book));
    }
}