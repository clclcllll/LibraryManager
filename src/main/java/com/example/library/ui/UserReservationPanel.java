package com.example.library.ui;

import com.example.library.dao.BookManager;
import com.example.library.dao.BorrowingManager;
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
public class UserReservationPanel extends JPanel {
    private Connection connection;
    private ReservationManager reservationManager;
    private BorrowingManager borrowingManager;
    private BookManager bookManager ;

    private JPanel inputPanel;
    private JTextField bookIdField;
    private JTextField usernameField;
    private JTextField reservationDateField;
    private JTextField bookStatusField;
    private JButton addButton;
    private JButton viewAvailableBooksButton;
    private JButton viewBorrowedBooksButton;

    private JButton searchBookstatusButton;
    private DefaultTableModel tableModel;
    private JTable bookTable;

    public UserReservationPanel(Connection connection) {
        this.connection = connection;
        this.reservationManager = new ReservationManager(connection);
        this.bookManager = new BookManager(connection);
        this.borrowingManager = new BorrowingManager(connection);

        initComponents();
        addListeners();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel();
        GroupLayout layout = new GroupLayout(inputPanel);
        inputPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel bookIdLabel = new JLabel("书籍名");
        bookIdField = new JTextField();
        JLabel usernameLabel = new JLabel("用户名:");
        usernameField = new JTextField();
        JLabel reservationDateLabel = new JLabel("预定日期:");
        reservationDateField = new JTextField();
        JLabel bookStatusLabel = new JLabel("查看书籍状态:");
        bookStatusField = new JTextField();


        addButton = new JButton("添加预定");

        viewAvailableBooksButton = new JButton("查看全部可借书籍");
        viewBorrowedBooksButton = new JButton("查看全部已借书籍");
        searchBookstatusButton = new JButton("搜索书籍状态");


        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(bookIdLabel)
                                        .addComponent(usernameLabel)
                                        .addComponent(reservationDateLabel)
                                        .addComponent(bookStatusLabel))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(bookIdField)
                                        .addComponent(usernameField)
                                        .addComponent(reservationDateField)
                                        .addComponent(bookStatusField)))
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(addButton)
                                .addComponent(viewAvailableBooksButton)
                                .addComponent(viewBorrowedBooksButton)
                                .addComponent(searchBookstatusButton)
                        )

        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(bookIdLabel)
                                .addComponent(bookIdField))
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
                                .addComponent(viewAvailableBooksButton)
                                .addComponent(viewBorrowedBooksButton)
                                .addComponent(searchBookstatusButton))
        );

        tableModel = new DefaultTableModel(new Object[]{"书名", "作者","状态", "借阅日期"}, 0);
        bookTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);

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

        viewAvailableBooksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAvailableBooks();
            }
        });

        viewBorrowedBooksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getBorrowedBooks();
            }
        });

        searchBookstatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBookstatus();
            }
        });
    }
    //添加预定
    private void addReservation() {
        String bookTitle = bookIdField.getText().trim();
        String username = usernameField.getText().trim();
        String reservationDate = reservationDateField.getText().trim();

        if (bookTitle.isEmpty() || username.isEmpty() || reservationDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请填写所有字段。", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Book book = bookManager.getBookByTitle(bookTitle);
        if (book == null) {
            JOptionPane.showMessageDialog(this, "没有找到相关书籍。", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        book.setUsername(username);
        book.setReservationDate(Date.valueOf(reservationDate));
        reservationManager.addReservation(book);
    }


    private void viewAvailableBooks() {
        List<Book> books = reservationManager.getAvailableBooks();
        tableModel.setRowCount(0); // 清空表格数据
        for (Book book : books) {
            tableModel.addRow(new Object[]{book.getTitle(), book.getAuthor(), "可借", ""});
        }
    }
    //搜索书籍状态
    private void searchBookstatus() {
        String bookTitle = bookStatusField.getText().trim();
        clearTexFields();
        tableModel.setRowCount(0); // 清空表格数据
        if (bookTitle.isEmpty()) {
            tableModel.setRowCount(0); // 清空表格数据
            JOptionPane.showMessageDialog(this, "请输入书籍名称以搜索。", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Book book = bookManager.getBookByTitle(bookTitle);
        if (book == null) {
            tableModel.setRowCount(0); // 清空表格数据
            JOptionPane.showMessageDialog(this, "没有找到相关书籍。", "信息", JOptionPane.INFORMATION_MESSAGE);
        }
        //判断是否借出
        if(borrowingManager.isBorrowed(book)){
            tableModel.addRow(new Object[]{
                    book.getTitle(), book.getAuthor(), "已借出", book.getBorrowingDate()
            });
        }else{
            tableModel.addRow(new Object[]{
                    book.getTitle(), book.getAuthor(),"可借", ""
            });
        }
    }

    private void getBorrowedBooks() {
        List<Book> books = reservationManager.getBorrowedBooks();
        tableModel.setRowCount(0); // 清空表格数据

        if (books.isEmpty()) {
            JOptionPane.showMessageDialog(this, "没有已借出的书籍", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // 遍历已借出的书籍
        for (Book book : books) {
            tableModel.addRow(new Object[]{book.getTitle(), book.getUsername(),  "已借出", book.getBorrowingDate()});
        }
    }

    private void clearTexFields() {
        bookIdField.setText("");
        usernameField.setText("");
        reservationDateField.setText("");
        bookStatusField.setText("");
    }
}
