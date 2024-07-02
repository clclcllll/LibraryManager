package com.example.library.dao;

import com.example.library.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationManager {
    private final Connection connection;

    public ReservationManager(Connection connection) {
        this.connection = connection;
    }

    public void addReservation(Book book) {
        String sql = "INSERT INTO reservations (book_id, username, reservation_date) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, book.getId());
            statement.setString(2, book.getUsername());
            statement.setDate(3, book.getReservationDate());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteReservation(int id) {
        String sql = "DELETE FROM reservations WHERE book_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateReservation(Book book) {
        String sql = "UPDATE reservations SET book_id = ?, username = ?, reservation_date = ? WHERE book_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, book.getId());
            statement.setString(2, book.getUsername());
            statement.setDate(3, book.getReservationDate());
            statement.setInt(4, book.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Book> getAllReservations() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM reservations";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setId(resultSet.getInt("book_id"));
                book.setAuthor(resultSet.getString("username"));
                book.setTitle(resultSet.getDate("reservation_date").toString());
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    //getBorrowedBooks()
    //通过borrowings表获取已借书籍列表
    public List<Book> getBorrowedBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT borrowings.book_id, borrowings.username, borrowings.borrowing_date, books.title, books.author, books.isbn " +
                "FROM borrowings " +
                "JOIN books ON borrowings.book_id = books.id";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("book_id"));
                book.setUsername(resultSet.getString("username"));
                book.setBorrowingDate(resultSet.getDate("borrowing_date"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setIsbn(resultSet.getString("isbn"));
                books.add(book);
            }
            System.out.println("获取所有已借书籍");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }



    // 获取可借书籍列表
    public List<Book> getAvailableBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM available_books";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setIsbn(resultSet.getString("isbn"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<Book> searchBookByTitle(String title) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + title + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setIsbn(resultSet.getString("isbn"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
    public boolean isBookAvailable(int bookId) {
        String sql = "SELECT COUNT(*) FROM available_books WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Date getBorrowingDate(int bookId) {
        String sql = "SELECT borrowing_date FROM borrowings WHERE book_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDate("borrowing_date");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //load
}
