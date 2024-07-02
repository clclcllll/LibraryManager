package com.example.library.ui;

import com.example.library.dao.BookManager;
import com.example.library.dao.ReservationManager;
import com.example.library.model.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class ReservationPanel extends JPanel {
    private Connection connection;
    private ReservationManager reservationManager;
    private BookManager bookManager;

    private JPanel inputPanel;

    private JTextField bookTitleField;
    private JTextField usernameField;
    private JTextField reservationDateField;
    private JTextField bookStatusField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton viewAvailableBooksButton;

    private JButton viewBorrowedBooksButton;
    private JButton searchBookButton;

    private JButton viewReservationsButton;
    private DefaultTableModel tableModel;
    private JTable bookTable;

    public ReservationPanel(Connection connection) {
        this.connection = connection;
        this.reservationManager = new ReservationManager(connection);
        this.bookManager = new BookManager(connection);


        initComponents();
        addListeners();
    }

    private void initComponents() {
        inputPanel = new JPanel();
        GroupLayout layout = new GroupLayout(inputPanel);
        inputPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel bookTitleLabel = new JLabel("书名:");
        bookTitleField = new JTextField();
        JLabel usernameLabel = new JLabel("用户名:");
        usernameField = new JTextField();
        JLabel reservationDateLabel = new JLabel("预定日期:");
        reservationDateField = new JTextField();
        JLabel bookStatusLabel = new JLabel("查看书籍状态:");
        bookStatusField = new JTextField();

        addButton = new JButton("添加预定");
        deleteButton = new JButton("删除预定");
        updateButton = new JButton("更新预定");
        viewAvailableBooksButton = new JButton("查看全部可借书籍");
        viewBorrowedBooksButton = new JButton("查看全部已借书籍");
        viewReservationsButton = new JButton("查看全部预定书籍");

        searchBookButton = new JButton("查询书籍状态");

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(bookTitleLabel)
                                .addComponent(usernameLabel)
                                .addComponent(reservationDateLabel)
                                .addComponent(bookStatusLabel))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(bookTitleField)
                                .addComponent(usernameField)
                                .addComponent(reservationDateField)
                                .addComponent(bookStatusField)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(addButton)
                                        .addComponent(deleteButton)
                                        .addComponent(updateButton)
                                        .addComponent(viewBorrowedBooksButton)
                                        .addComponent(viewAvailableBooksButton)
                                        .addComponent(searchBookButton)
                                        .addComponent(viewReservationsButton)))
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(bookTitleLabel)
                                .addComponent(bookTitleField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(usernameLabel)
                                .addComponent(usernameField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(reservationDateLabel)
                                .addComponent(reservationDateField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(bookStatusLabel)
                                .addComponent(bookStatusField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(addButton)
                                .addComponent(deleteButton)
                                .addComponent(updateButton)
                                .addComponent(viewBorrowedBooksButton)
                                .addComponent(viewAvailableBooksButton)
                                .addComponent(searchBookButton)
                                .addComponent(viewReservationsButton))
        );
        tableModel = new DefaultTableModel(new Object[]{"书籍ID", "书名", "作者", "ISBN", "状态", "借阅日期"}, 0);
        bookTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);

        setLayout(new BorderLayout(10,10));
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addListeners() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addReservation();
                clearTexFields();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteReservation();
                clearTexFields();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateReservation();
                tableModel.setRowCount(0); // 清空表格数据
                clearTexFields();
            }
        });

        viewAvailableBooksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAvailableBooks();
            }
        });

        //viewBorrowedBooksButton.addActionListener() method
        viewBorrowedBooksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getBorrowedBooks();
            }
        });

        searchBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBook();
            }
        });
        //viewReservationsButton.addActionListener() method
        viewReservationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadReservations();
            }
        });

    }

    private void addReservation() {
        try {
            String bookTitle = bookTitleField.getText().trim();
            String username = usernameField.getText().trim();
            Date reservationDate = Date.valueOf(reservationDateField.getText().trim());

            if (username.isEmpty() || reservationDate == null) {
                JOptionPane.showMessageDialog(this, "所有字段都是必填项", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Book book = bookManager.getBookByTitle(bookTitle);
            if (book == null) {
                JOptionPane.showMessageDialog(this, "没有找到相关书籍", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            book.setUsername(username);
            book.setReservationDate(reservationDate);
            reservationManager.addReservation(book);
            JOptionPane.showMessageDialog(this, "预定信息添加成功");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "输入格式有误，请检查输入内容", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteReservation() {
        try {
            String bookTitle = bookTitleField.getText().trim();
            Book book = bookManager.getBookByTitle(bookTitle);
            int bookId = book.getId();
            reservationManager.deleteReservation(bookId);
            JOptionPane.showMessageDialog(this, "预定信息删除成功");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "输入格式有误，请检查输入内容", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateReservation() {
        try {
            int bookId=bookManager.getBookByTitle(bookTitleField.getText().trim()).getId();
            String username = usernameField.getText().trim();
            Date reservationDate = Date.valueOf(reservationDateField.getText().trim());

            if (username.isEmpty() || reservationDate == null) {
                JOptionPane.showMessageDialog(this, "所有字段都是必填项", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Book book = new Book(bookId, username, reservationDate);
            book.setReservationDate(reservationDate);
            book.setUsername(username);
            book.setId(bookId);

            reservationManager.updateReservation(book);
            JOptionPane.showMessageDialog(this, "预定信息更新成功");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "输入格式有误，请检查输入内容", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewAvailableBooks() {
        List<Book> books = reservationManager.getAvailableBooks();
        tableModel.setRowCount(0); // 清空表格数据
        for (Book book : books) {
            tableModel.addRow(new Object[]{book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn(), "可借", ""});
        }
    }

    private void searchBook() {
        String bookTitle = bookStatusField.getText().trim();
        if (bookTitle.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入书籍名称", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<Book> books = reservationManager.searchBookByTitle(bookTitle);
        tableModel.setRowCount(0); // 清空表格数据
        for (Book book : books) {
            String status = "可借";
            String borrowingDate = "";
            if (!reservationManager.isBookAvailable(book.getId())) {
                status = "已借出";
                borrowingDate = reservationManager.getBorrowingDate(book.getId()).toString();
            }
            tableModel.addRow(new Object[]{book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn(), status, borrowingDate});
        }

        if (books.isEmpty()) {
            JOptionPane.showMessageDialog(this, "未找到指定的书籍", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    //getBorrowedBooks() method
    private void getBorrowedBooks() {
        List<Book> books = reservationManager.getBorrowedBooks();
        tableModel.setRowCount(0); // 清空表格数据

        if(books.isEmpty()){
            JOptionPane.showMessageDialog(this, "没有已借出的书籍", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // 遍历已借出的书籍
        for (Book book : books) {
            tableModel.addRow(new Object[]{book.getId(),book.getTitle(),book.getUsername(),"已借出", book.getBorrowingDate(),"",""});
        }
    }
    //loadReservations() method
    private void loadReservations() {
        List<Book> books = reservationManager.getAllReservations();
        tableModel.setRowCount(0); // 清空表格数据
        for (Book book : books) {
            String bookTitle = bookManager.getBookById(book.getId()).getTitle();
            tableModel.addRow(new Object[]{"", bookTitle, book.getUsername(), "", "已预定", book.getReservationDate()});
        }
    }
    //clearTexFields() method
    private void clearTexFields() {
        bookTitleField.setText("");
        usernameField.setText("");
        reservationDateField.setText("");
        bookStatusField.setText("");
    }

}