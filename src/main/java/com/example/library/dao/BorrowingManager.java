package com.example.library.dao;

import com.example.library.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowingManager {
    private final Connection connection;

    public BorrowingManager(Connection connection) {
        this.connection = connection;
    }

    public List<Book> getAllBorrowings() {
        List<Book> borrowings = new ArrayList<>();
        String sql = "SELECT borrowings.id, books.title, books.author, books.isbn, borrowings.username, borrowings.borrowing_date FROM borrowings JOIN books ON borrowings.book_id = books.id";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setIsbn(resultSet.getString("isbn"));
                book.setUsername(resultSet.getString("username"));
                book.setBorrowingDate(resultSet.getDate("borrowing_date"));
                borrowings.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowings;
    }

    public void addBorrowing(Book book) {
        String getBookIdSql = "SELECT id FROM books WHERE title = ?";
        String insertBorrowingSql = "INSERT INTO borrowings (book_id, username, borrowing_date) VALUES (?, ?, ?)";
        String deleteAvailableBookSql = "DELETE FROM available_books WHERE id = ?";//这里一开始写成了where book_id = ?，导致删除不了（ -_- ）
        try (PreparedStatement getBookIdStatement = connection.prepareStatement(getBookIdSql);
             PreparedStatement insertBorrowingStatement = connection.prepareStatement(insertBorrowingSql);
             PreparedStatement deleteAvailableBookStatement = connection.prepareStatement(deleteAvailableBookSql)
        ) {

            getBookIdStatement.setString(1, book.getTitle());
            ResultSet resultSet = getBookIdStatement.executeQuery();

            if (resultSet.next()) {
                int bookId = resultSet.getInt("id");
                insertBorrowingStatement.setInt(1, bookId);
                insertBorrowingStatement.setString(2, book.getUsername());
                insertBorrowingStatement.setDate(3, book.getBorrowingDate());
                insertBorrowingStatement.executeUpdate();

                deleteAvailableBookStatement.setInt(1, bookId);
                deleteAvailableBookStatement.executeUpdate();

                System.out.println("成功插入借阅，并在可借书表中删除记录");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBorrowing(Book book) {
        String getBookIdSql = "SELECT id FROM books WHERE title = ?";
        String getBorrowingIdSql = "SELECT id, book_id FROM borrowings WHERE book_id = ? AND username = ? AND borrowing_date = ?";
        String deleteBorrowingSql = "DELETE FROM borrowings WHERE id = ?";
        String insertAvailableBookSql = "INSERT INTO available_books (id, title, author, isbn) SELECT id, title, author, isbn FROM books WHERE id = ?";

        try (PreparedStatement getBookIdStatement = connection.prepareStatement(getBookIdSql);
             PreparedStatement getBorrowingIdStatement = connection.prepareStatement(getBorrowingIdSql);
             PreparedStatement deleteBorrowingStatement = connection.prepareStatement(deleteBorrowingSql);
             PreparedStatement insertAvailableBookStatement = connection.prepareStatement(insertAvailableBookSql)) {

            // 获取书籍ID
            getBookIdStatement.setString(1, book.getTitle());
            ResultSet bookIdResultSet = getBookIdStatement.executeQuery();
            if (bookIdResultSet.next()) {
                int bookId = bookIdResultSet.getInt("id");

                // 获取借阅记录ID
                getBorrowingIdStatement.setInt(1, bookId);
                getBorrowingIdStatement.setString(2, book.getUsername());
                getBorrowingIdStatement.setDate(3, book.getBorrowingDate());
                ResultSet borrowingResultSet = getBorrowingIdStatement.executeQuery();
                if (borrowingResultSet.next()) {
                    int borrowingId = borrowingResultSet.getInt("id");

                    // 删除借阅记录
                    deleteBorrowingStatement.setInt(1, borrowingId);
                    deleteBorrowingStatement.executeUpdate();

                    // 插入到可借书籍表
                    insertAvailableBookStatement.setInt(1, bookId);
                    insertAvailableBookStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBorrowing(Book book) {
        String getBookIdSql = "SELECT id FROM books WHERE title = ?";
        String updateBorrowingSql = "UPDATE borrowings SET book_id = ?, username = ?, borrowing_date = ? WHERE id = ?";
        try (PreparedStatement getBookIdStatement = connection.prepareStatement(getBookIdSql);
             PreparedStatement updateBorrowingStatement = connection.prepareStatement(updateBorrowingSql)) {
            getBookIdStatement.setString(1, book.getTitle());
            ResultSet resultSet = getBookIdStatement.executeQuery();
            if (resultSet.next()) {
                int bookId = resultSet.getInt("id");
                updateBorrowingStatement.setInt(1, bookId);
                updateBorrowingStatement.setString(2, book.getUsername());
                updateBorrowingStatement.setDate(3, book.getBorrowingDate());
                updateBorrowingStatement.setInt(4, book.getId());
                updateBorrowingStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Book> searchBorrowingsByUsername(String username) {
        List<Book> borrowings = new ArrayList<>();
        String sql = "SELECT borrowings.id, books.title, books.author, books.isbn, borrowings.username, borrowings.borrowing_date FROM borrowings JOIN books ON borrowings.book_id = books.id WHERE borrowings.username = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setIsbn(resultSet.getString("isbn"));
                book.setUsername(resultSet.getString("username"));
                book.setBorrowingDate(resultSet.getDate("borrowing_date"));
                borrowings.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowings;
    }
    public void returnBorrowing(Book book) {
        String getBookIdSql = "SELECT id FROM books WHERE title = ?";
        String getBorrowingSql = "SELECT id, book_id FROM borrowings WHERE book_id = ? AND username = ? AND borrowing_date = ?";
        String deleteBorrowingSql = "DELETE FROM borrowings WHERE id = ?";
        String insertAvailableBookSql = "INSERT INTO available_books (id, title, author, isbn) SELECT id, title, author, isbn FROM books WHERE id = ?";

        try (PreparedStatement getBookIdStatement = connection.prepareStatement(getBookIdSql);
             PreparedStatement getBorrowingStatement = connection.prepareStatement(getBorrowingSql);
             PreparedStatement deleteBorrowingStatement = connection.prepareStatement(deleteBorrowingSql);
             PreparedStatement insertAvailableBookStatement = connection.prepareStatement(insertAvailableBookSql)) {

            // 获取书籍ID
            getBookIdStatement.setString(1, book.getTitle());
            ResultSet bookIdResultSet = getBookIdStatement.executeQuery();

            if (bookIdResultSet.next()) {
                int bookId = bookIdResultSet.getInt("id");

               //
                getBorrowingStatement.setInt(1, bookId);
                getBorrowingStatement.setString(2, book.getUsername());
                getBorrowingStatement.setDate(3, book.getBorrowingDate());

                ResultSet borrowingResultSet = getBorrowingStatement.executeQuery();

                if (borrowingResultSet.next()) {
                    int borrowingId = borrowingResultSet.getInt("id");


                    deleteBorrowingStatement.setInt(1, borrowingId);
                    deleteBorrowingStatement.executeUpdate();


                    insertAvailableBookStatement.setInt(1, bookId);
                    insertAvailableBookStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //是否借出
    public boolean isBorrowed(Book book) {
        String sql = "SELECT * FROM borrowings WHERE book_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, book.getId());
            ResultSet resultSet = statement.executeQuery();
            //如果有结果，说明已经借出
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
