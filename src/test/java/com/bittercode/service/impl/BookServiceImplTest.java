package com.bittercode.service.impl;

import com.bittercode.constant.ResponseCode;
import com.bittercode.model.Book;
import com.bittercode.model.StoreException;
import com.bittercode.util.DBUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void deveRetornarSucessoAoAdicionarLivroValido() throws Exception {
        Book book = new Book("ISBN-001", "Effective Java", "Joshua Bloch", 120.0, 5);

        try (MockedStatic<DBUtil> dbUtilMock = mockStatic(DBUtil.class)) {
            dbUtilMock.when(DBUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            String result = bookService.addBook(book);

            assertEquals(ResponseCode.SUCCESS.name(), result);
            verify(mockPreparedStatement).setString(1, "ISBN-001");
            verify(mockPreparedStatement).setString(2, "Effective Java");
            verify(mockPreparedStatement).setDouble(4, 120.0);
            verify(mockPreparedStatement).setInt(5, 5);
            verify(mockPreparedStatement).executeUpdate();
        }
    }

    @Test
    void deveAtualizarEstoqueCorretamente() throws Exception {
        try (MockedStatic<DBUtil> dbUtilMock = mockStatic(DBUtil.class)) {
            dbUtilMock.when(DBUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            String result = bookService.updateBookQtyById("ISBN-001", 15);

            assertEquals(ResponseCode.SUCCESS.name(), result);
            verify(mockPreparedStatement).setInt(1, 15);
            verify(mockPreparedStatement).setString(2, "ISBN-001");
            verify(mockPreparedStatement).executeUpdate();
        }
    }

    @Test
    void deveRetornarFailureAoDeletarLivroInexistente() throws Exception {
        try (MockedStatic<DBUtil> dbUtilMock = mockStatic(DBUtil.class)) {
            dbUtilMock.when(DBUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(0);

            String result = bookService.deleteBookById("ISBN-INEXISTENTE");

            assertEquals(ResponseCode.FAILURE.name(), result);
        }
    }

    @Test
    void deveRetornarListaVaziaQuandoIdsForemVaziosSemAcessarBanco() throws Exception {
        try (MockedStatic<DBUtil> dbUtilMock = mockStatic(DBUtil.class)) {
            List<Book> result = bookService.getBooksByCommaSeperatedBookIds("   ");

            assertTrue(result.isEmpty());
            dbUtilMock.verifyNoInteractions();
        }
    }

    @Test
    void deveLancarStoreExceptionQuandoOcorrerErroDeSql() throws Exception {
        try (MockedStatic<DBUtil> dbUtilMock = mockStatic(DBUtil.class)) {
            dbUtilMock.when(DBUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Erro simulado"));

            assertThrows(StoreException.class, () -> bookService.getAllBooks());
        }
    }
}