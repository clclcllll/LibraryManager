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

public class UserBorrowingPanel extends JPanel {
    private Connection connection;
    private BorrowingManager borrowingManager;
    private BookManager bookManager;
    private ReservationManager reservationManager;

    private JTextField bookTitleField;
    private JTextField usernameField;
    private JTextField borrowingDateField;
    private JTextField searchBookField;
    private JButton borrowButton;
    private JButton viewAvailableBooksButton;
    private JButton viewAllBooksButton;
    private JButton viewMyBorrowingsButton;
    private JButton searchBookButton;
    private JTable borrowingTable;
    private DefaultTableModel tableModel;

    public UserBorrowingPanel(Connection connection) {
        this.connection = connection;
        this.borrowingManager = new BorrowingManager(connection);
        this.bookManager = new BookManager(connection);
        this.reservationManager= new ReservationManager(connection);

        initComponents();
        addListeners();
        loadBorrowedBooks();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel();
        GroupLayout layout = new GroupLayout(inputPanel);
        inputPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel bookTitleLabel = new JLabel("书籍名称:");
        bookTitleField = new JTextField();
        JLabel usernameLabel = new JLabel("用户名:");
        usernameField = new JTextField();
        JLabel borrowingDateLabel = new JLabel("借阅日期:");
        borrowingDateField = new JTextField();
        JLabel searchBookLabel = new JLabel("查询书籍状态:");
        searchBookField = new JTextField();

        borrowButton = new JButton("借阅书籍");
        viewAvailableBooksButton = new JButton("查看可借书籍");
        viewAllBooksButton = new JButton("查看所有书籍");
        viewMyBorrowingsButton = new JButton("查看我的借阅");
        searchBookButton = new JButton("查询书籍状态");


        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(bookTitleLabel)
                                        .addComponent(usernameLabel)
                                        .addComponent(borrowingDateLabel)
                                        .addComponent(searchBookLabel))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(bookTitleField)
                                        .addComponent(usernameField)
                                        .addComponent(borrowingDateField)
                                        .addComponent(searchBookField)))
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(borrowButton)
                                .addComponent(viewAvailableBooksButton)
                                .addComponent(viewAllBooksButton)
                                .addComponent(viewMyBorrowingsButton)
                                .addComponent(searchBookButton))
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
                                .addComponent(borrowingDateLabel)
                                .addComponent(borrowingDateField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(searchBookLabel)
                                .addComponent(searchBookField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(borrowButton)
                                .addComponent(viewAvailableBooksButton)
                                .addComponent(viewAllBooksButton)
                                .addComponent(viewMyBorrowingsButton)
                                .addComponent(searchBookButton))
        );

        tableModel = new DefaultTableModel(new Object[]{"书名", "用户名", "借阅日期"}, 0);

        borrowingTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(borrowingTable);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addListeners() {
        borrowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                borrowBook();
            }
        });

        viewAvailableBooksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAvailableBooks();
            }
        });

        viewAllBooksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAllBooks();
            }
        });

        viewMyBorrowingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewMyBorrowings();
            }
        });
        searchBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBook();
            }
        });

        borrowingTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = borrowingTable.rowAtPoint(evt.getPoint());
                if (row < 0) {
                    return;
                }
                bookTitleField.setText(borrowingTable.getValueAt(row, 0).toString());
            }
        });
    }
    //搜索书籍
    private void searchBook() {
        String bookTitle = searchBookField.getText().trim();
        clearTextFields();
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
                    book.getTitle(), book.getAuthor(), "已借出"
            });
        }else{
            tableModel.addRow(new Object[]{
                    book.getTitle(), book.getAuthor(), "可借"
            });
        }
    }
    private void viewMyBorrowings() {
        bookTitleField.setText("");
        borrowingDateField.setText("");

        String username = usernameField.getText().trim();
        if (username.isEmpty()) {
            tableModel.setRowCount(0); // 清空表格数据
            JOptionPane.showMessageDialog(this, "请输入用户名以查看借阅信息。", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Book> books = borrowingManager.searchBorrowingsByUsername(username);
        tableModel.setRowCount(0); // 清空表格数据
        for (Book book : books) {
            tableModel.addRow(new Object[]{book.getTitle(), book.getUsername(), book.getBorrowingDate()});
        }

        if (books.isEmpty()) {
            JOptionPane.showMessageDialog(this, "您当前没有借阅的书籍。", "信息", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void borrowBook() {
        try {
            String bookTitle = bookTitleField.getText().trim();
            String username = usernameField.getText().trim();
            Date borrowingDate = Date.valueOf(borrowingDateField.getText().trim());

            if (bookTitle.isEmpty() || username.isEmpty() || borrowingDate == null) {
                JOptionPane.showMessageDialog(this, "所有字段都是必填项", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<Book> userBorrowings = borrowingManager.searchBorrowingsByUsername(username);
            if (!userBorrowings.isEmpty()) {
                JOptionPane.showMessageDialog(this, "您已经有借阅的书籍，请先归还书籍再借阅新的书籍。", "错误", JOptionPane.ERROR_MESSAGE);
                loadBorrowedBooks();
                return;
            }

            Book book =bookManager.getBookByTitle(bookTitle);
            //判断是否存在该书籍
            if (book == null) {
                JOptionPane.showMessageDialog(this, "没有找到相关书籍。", "信息", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            //判断是否借出
            if(borrowingManager.isBorrowed(book)){
                JOptionPane.showMessageDialog(this, "书籍已借出", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Book rbook=new Book(bookTitle,username,borrowingDate);
            rbook.setTitle(book.getTitle());
            rbook.setUsername(username);
            rbook.setBorrowingDate(borrowingDate);

            borrowingManager.addBorrowing(rbook);
            JOptionPane.showMessageDialog(this, "书籍借阅成功");
            clearTextFields();
            loadBorrowedBooks();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "输入格式有误，请检查输入内容", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewAvailableBooks() {
        clearTextFields();
        List<Book> books = reservationManager.getAvailableBooks();
        if (books.isEmpty()) {
            tableModel.setRowCount(0); // 清空表格数据
            JOptionPane.showMessageDialog(this, "目前没有可借书籍", "提示", JOptionPane.INFORMATION_MESSAGE);
        }
        tableModel.setRowCount(0); // 清空表格数据
        for (Book book : books) {
            tableModel.addRow(new Object[]{book.getTitle(), book.getAuthor(), "可借"});
        }
    }

    private void viewAllBooks() {
        clearTextFields();
        List<Book> books = bookManager.getAllBooks();
        tableModel.setRowCount(0); // 清空表格数据
        for (Book book : books) {
            tableModel.addRow(new Object[]{book.getTitle(), book.getAuthor(), ""});
        }
    }

    private void loadBorrowedBooks() {
        List<Book> books = borrowingManager.getAllBorrowings();
        tableModel.setRowCount(0); // 清空表格数据
        for (Book book : books) {
            tableModel.addRow(new Object[]{book.getTitle(), book.getUsername(), book.getBorrowingDate()});
        }
    }

    private void clearTextFields() {
        bookTitleField.setText("");
        usernameField.setText("");
        borrowingDateField.setText("");
    }
}
