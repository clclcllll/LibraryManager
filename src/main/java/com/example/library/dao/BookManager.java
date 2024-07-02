package com.example.library.dao;

import com.example.library.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookManager {
    private final Connection connection;

    public BookManager(Connection connection) {
        this.connection = connection;
    }

    public void addBook(Book book) throws SQLIntegrityConstraintViolationException {
        String title = book.getTitle();
        String author = book.getAuthor();
        String isbn = book.getIsbn();
        String publisher = book.getPublisherName();

        // 检查书籍信息是否为空
        if (title == null || title.isEmpty() || author == null || author.isEmpty() || isbn == null || isbn.isEmpty()) {
            System.out.println("书籍标题、作者和ISBN不能为空");
            return;
        }

        String sql = "INSERT INTO books (title, author, isbn, publisher) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, title);
            statement.setString(2, author);
            statement.setString(3, isbn);
            statement.setString(4, publisher);
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                book.setId(generatedKeys.getInt(1));
            }
            System.out.println("书籍添加成功");
            // 添加至可借书籍表
            addAvailableBook(book);
        } catch (SQLIntegrityConstraintViolationException e) {
            // 捕获重复插入异常并抛出，以便在UI层处理
            throw e;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBook(int id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("书籍删除成功");
            // 同步删除可借书籍和已借书籍表中的相应书籍
            deleteAvailableBook(id);
            deleteBorrowedBook(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBook(Book book) {
        String title = book.getTitle();
        String author = book.getAuthor();
        String isbn = book.getIsbn();
        String publisher = book.getPublisherName();
        int id = book.getId();

        // 检查书籍信息是否为空
        if (title == null || title.isEmpty() || author == null || author.isEmpty() || isbn == null || isbn.isEmpty()) {
            System.out.println("书籍标题、作者和ISBN不能为空");
            return;
        }

        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, publisher = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            statement.setString(2, author);
            statement.setString(3, isbn);
            statement.setString(4, publisher);
            statement.setInt(5, id);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("书籍更新成功");
                // 同步更新可借书籍表中的相应条目
                updateAvailableBook(book);
            } else {
                System.out.println("没有找到要更新的书籍");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setIsbn(resultSet.getString("isbn"));
                book.setPublisherName(resultSet.getString("publisher"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public void addAvailableBook(Book book) {
        String sql = "INSERT INTO available_books (id, title, author, isbn) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, book.getId());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setString(4, book.getIsbn());
            statement.executeUpdate();
            System.out.println("添加可借书籍成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAvailableBook(int id) {
        String sql = "DELETE FROM available_books WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("可借书籍删除成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addBorrowedBook(Book book) {
        String sql = "INSERT INTO borrowed_books (id, title, author, isbn) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, book.getId());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setString(4, book.getIsbn());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBorrowedBook(int id) {
        String sql = "DELETE FROM borrowed_books WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAvailableBook(Book book) {
        String sql = "UPDATE available_books SET title = ?, author = ?, isbn = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getIsbn());
            statement.setInt(4, book.getId());
            statement.executeUpdate();
            System.out.println("可借书籍更新成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //getBookByTitle
    public Book getBookByTitle(String title) {
        String sql = "SELECT * FROM books WHERE title = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setIsbn(resultSet.getString("isbn"));
                return book;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //getBookById
    public Book getBookById(int id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setIsbn(resultSet.getString("isbn"));
                return book;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void clearAvailableBooks() throws SQLException {
        String sql = "DELETE FROM available_books";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            System.out.println("清空可借书籍表成功");
        }
    }
    // 初始化已借书籍表
    public void initBorrowedBooks() {
        // 清空已借书籍表
        clearBorrowedBooks();
        System.out.println("初始化已借书籍表成功");
    }
    // 导入图书信息到可借书籍表
    public void importBooksToAvailableBooks() {
        try {
            // 清空可借书籍表,再导入
            clearAvailableBooks(); // 清空 available_books 表
            List<Book> books = getAllBooks();
            for (Book book : books) {
                addAvailableBook(new Book(book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn()));
            }
            System.out.println("导入成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void clearBorrowedBooks() {
        String sql = "DELETE FROM borrowings";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            System.out.println("清空已借书籍表成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
