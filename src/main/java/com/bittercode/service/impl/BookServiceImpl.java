package com.bittercode.service.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.bittercode.constant.ResponseCode;
import com.bittercode.constant.db.BooksDBConstants;
import com.bittercode.model.Book;
import com.bittercode.model.StoreException;
import com.bittercode.service.BookService;
import com.bittercode.util.DBUtil;

public class BookServiceImpl implements BookService {

    private static final String GET_ALL_BOOKS_QUERY = "SELECT * FROM " + BooksDBConstants.TABLE_BOOK;
    private static final String GET_BOOK_BY_ID_QUERY = "SELECT * FROM " + BooksDBConstants.TABLE_BOOK
            + " WHERE " + BooksDBConstants.COLUMN_BARCODE + " = ?";

    private static final String DELETE_BOOK_BY_ID_QUERY = "DELETE FROM " + BooksDBConstants.TABLE_BOOK + "  WHERE "
            + BooksDBConstants.COLUMN_BARCODE + "=?";

    private static final String ADD_BOOK_QUERY = "INSERT INTO " + BooksDBConstants.TABLE_BOOK + "  VALUES(?,?,?,?,?)";

    private static final String UPDATE_BOOK_QTY_BY_ID_QUERY = "UPDATE " + BooksDBConstants.TABLE_BOOK + " SET "
            + BooksDBConstants.COLUMN_QUANTITY + "=? WHERE " + BooksDBConstants.COLUMN_BARCODE
            + "=?";

    private static final String UPDATE_BOOK_BY_ID_QUERY = "UPDATE " + BooksDBConstants.TABLE_BOOK + " SET "
            + BooksDBConstants.COLUMN_NAME + "=? , "
            + BooksDBConstants.COLUMN_AUTHOR + "=?, "
            + BooksDBConstants.COLUMN_PRICE + "=?, "
            + BooksDBConstants.COLUMN_QUANTITY + "=? "
            + "  WHERE " + BooksDBConstants.COLUMN_BARCODE
            + "=?";

    @Override
    public Book getBookById(String bookId) throws StoreException {
        Book book = null;

        try (Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(GET_BOOK_BY_ID_QUERY)) {

            ps.setString(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    book = mapResultSetToBook(rs);
                }
            }

        } catch (SQLException e) {
            throw new StoreException(ResponseCode.INTERNAL_SERVER_ERROR.name(),
                    "Erro ao buscar livro por ID: " + e.getMessage());
        }
        return book;
    }

    @Override
    public List<Book> getAllBooks() throws StoreException {
        List<Book> books = new ArrayList<Book>();

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_ALL_BOOKS_QUERY);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }

        } catch (SQLException e) {
            throw new StoreException(ResponseCode.INTERNAL_SERVER_ERROR.name(),
                    "Erro ao buscar livros: " + e.getMessage());
        }
        return books;
    }

    @Override
    public String deleteBookById(String bookId) throws StoreException {
        String response = ResponseCode.FAILURE.name();

        try (Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(DELETE_BOOK_BY_ID_QUERY)){

            ps.setString(1, bookId);

            int k = ps.executeUpdate();
            if (k == 1) {
                response = ResponseCode.SUCCESS.name();
            }

        } catch (SQLException e) {
            throw new StoreException(ResponseCode.INTERNAL_SERVER_ERROR.name(),
                    "Erro ao deletar livro: " + e.getMessage());
        }
        return response;
    }

    @Override
    public String addBook(Book book) throws StoreException {
        String responseCode = ResponseCode.FAILURE.name();

        try (Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(ADD_BOOK_QUERY)){

            ps.setString(1, book.getBarcode());
            ps.setString(2, book.getName());
            ps.setString(3, book.getAuthor());
            ps.setDouble(4, book.getPrice());
            ps.setInt(5, book.getQuantity());

            int k = ps.executeUpdate();
            if (k == 1) {
                responseCode = ResponseCode.SUCCESS.name();
            }

        } catch (SQLException e) {
            throw new StoreException(ResponseCode.INTERNAL_SERVER_ERROR.name(),
                    "Erro ao adicionar livro: " + e.getMessage());
        }
        return responseCode;
    }

    @Override
    public String updateBookQtyById(String bookId, int quantity) throws StoreException {
        String responseCode = ResponseCode.FAILURE.name();

        try (Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(UPDATE_BOOK_QTY_BY_ID_QUERY)){

            ps.setInt(1, quantity);
            ps.setString(2, bookId);
            ps.executeUpdate();
            responseCode = ResponseCode.SUCCESS.name();

        } catch (SQLException e) {
            throw new StoreException(ResponseCode.INTERNAL_SERVER_ERROR.name(),
                    "Erro ao atualizar livro por ID: " + e.getMessage());
        }
        return responseCode;
    }

    @Override
    public List<Book> getBooksByCommaSeperatedBookIds(String commaSeperatedBookIds) throws StoreException {
        if (commaSeperatedBookIds == null || commaSeperatedBookIds.trim().isEmpty()) {
            return Collections.emptyList();
        }

        List<String> bookIds = Arrays.stream(commaSeperatedBookIds.split(","))
                .map(String::trim)
                .filter(id -> !id.isEmpty())
                .collect(Collectors.toList());

        if (bookIds.isEmpty()) {
            return Collections.emptyList();
        }

        String placeholders = String.join(",", Collections.nCopies(bookIds.size(), "?"));
        String query = "SELECT * FROM " + BooksDBConstants.TABLE_BOOK
                + " WHERE " + BooksDBConstants.COLUMN_BARCODE + " IN ( " + placeholders + " )";

        List<Book> books = new ArrayList<Book>();

        try (Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {

            for (int i = 0; i < bookIds.size(); i++) {
                ps.setString(i + 1, bookIds.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }

        } catch (SQLException e) {
            throw new StoreException(ResponseCode.INTERNAL_SERVER_ERROR.name(),
                    "Erro ao buscar livros por IDs: " + e.getMessage());
        }
        return books;
    }

    @Override
    public String updateBook(Book book) throws StoreException {
        String responseCode = ResponseCode.FAILURE.name();

        try (Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(UPDATE_BOOK_BY_ID_QUERY)){

            ps.setString(1, book.getName());
            ps.setString(2, book.getAuthor());
            ps.setDouble(3, book.getPrice());
            ps.setInt(4, book.getQuantity());
            ps.setString(5, book.getBarcode());
            ps.executeUpdate();
            responseCode = ResponseCode.SUCCESS.name();

        } catch (SQLException e) {
            throw new StoreException(ResponseCode.INTERNAL_SERVER_ERROR.name(),
                    "Erro ao atualizar livro: " + e.getMessage());
        }
        return responseCode;
    }

    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        return new Book(
                rs.getString(BooksDBConstants.COLUMN_BARCODE),
                rs.getString(BooksDBConstants.COLUMN_NAME),
                rs.getString(BooksDBConstants.COLUMN_AUTHOR),
                rs.getDouble(BooksDBConstants.COLUMN_PRICE),
                rs.getInt(BooksDBConstants.COLUMN_QUANTITY)
        );
    }
}
