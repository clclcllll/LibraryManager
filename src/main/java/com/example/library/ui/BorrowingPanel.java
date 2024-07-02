package com.example.library.ui;

import com.example.library.dao.BorrowingManager;
import com.example.library.model.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class BorrowingPanel extends JPanel {
    private Connection connection;
    private BorrowingManager borrowingManager;

    private JTextField bookTitleField;
    private JTextField usernameField;
    private JTextField borrowingDateField;
    private JTextField searchField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton searchButton;
    private JTable borrowingTable;
    private DefaultTableModel tableModel;

    public BorrowingPanel(Connection connection) {
        this.connection = connection;
        this.borrowingManager = new BorrowingManager(connection);

        initComponents();
        addListeners();
        loadBorrowings(); // Load all borrowings on initialization
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
        JLabel searchLabel = new JLabel("搜索借阅:");
        searchField = new JTextField();

        addButton = new JButton("添加借阅");
        deleteButton = new JButton("删除借阅");
        updateButton = new JButton("更新借阅");
        searchButton = new JButton("搜索借阅");

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(bookTitleLabel)
                                        .addComponent(usernameLabel)
                                        .addComponent(borrowingDateLabel)
                                        .addComponent(searchLabel))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(bookTitleField)
                                        .addComponent(usernameField)
                                        .addComponent(borrowingDateField)
                                        .addComponent(searchField)))
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(addButton)
                                .addComponent(deleteButton)
                                .addComponent(updateButton)
                                .addComponent(searchButton))
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
                                .addComponent(searchLabel)
                                .addComponent(searchField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(addButton)
                                .addComponent(deleteButton)
                                .addComponent(updateButton)
                                .addComponent(searchButton))
        );

        tableModel = new DefaultTableModel(new Object[]{"书名", "用户名", "借阅日期"}, 0);

        borrowingTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(borrowingTable);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addListeners() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBorrowing();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBorrowing();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBorrowing();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBorrowing();
            }
        });

        // 鼠标点击表格事件
        borrowingTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = borrowingTable.rowAtPoint(evt.getPoint());
                if (row < 0) {
                    return;
                }
                bookTitleField.setText(borrowingTable.getValueAt(row, 0).toString());
                usernameField.setText(borrowingTable.getValueAt(row, 1).toString());
                borrowingDateField.setText(borrowingTable.getValueAt(row, 2).toString());
            }
        });
    }

    private void addBorrowing() {
        try {
            String bookTitle = bookTitleField.getText().trim();
            String username = usernameField.getText().trim();
            Date borrowingDate = Date.valueOf(borrowingDateField.getText().trim());

            if (bookTitle.isEmpty() || username.isEmpty() || borrowingDate == null) {
                JOptionPane.showMessageDialog(this, "所有字段都是必填项", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Book book = new Book(bookTitle, username, borrowingDate);

            borrowingManager.addBorrowing(book);
            JOptionPane.showMessageDialog(this, "借阅信息添加成功");
            clearTextFields();
            loadBorrowings();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "输入格式有误，请检查输入内容", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBorrowing() {
        try {
            String bookTitle = bookTitleField.getText().trim();
            String username = usernameField.getText().trim();
            Date borrowingDate = Date.valueOf(borrowingDateField.getText().trim());

            if (bookTitle.isEmpty() || username.isEmpty() || borrowingDate == null) {
                JOptionPane.showMessageDialog(this, "所有字段都是必填项", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Book book = new Book(bookTitle, username, borrowingDate);

            borrowingManager.deleteBorrowing(book);
            JOptionPane.showMessageDialog(this, "借阅信息删除成功");
            clearTextFields();
            loadBorrowings();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "输入格式有误，请检查输入内容", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBorrowing() {
        try {
            String bookTitle = bookTitleField.getText().trim();
            String username = usernameField.getText().trim();
            Date borrowingDate = Date.valueOf(borrowingDateField.getText().trim());

            if (bookTitle.isEmpty() || username.isEmpty() || borrowingDate == null) {
                JOptionPane.showMessageDialog(this, "所有字段都是必填项", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Book book = new Book(bookTitle, username, borrowingDate);

            borrowingManager.updateBorrowing(book);
            JOptionPane.showMessageDialog(this, "借阅信息更新成功");
            clearTextFields();
            loadBorrowings();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "输入格式有误，请检查输入内容", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchBorrowing() {
        String searchQuery = searchField.getText().trim();
        if (searchQuery.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入要搜索的借阅信息", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Book> books = borrowingManager.searchBorrowingsByUsername(searchQuery);
        tableModel.setRowCount(0); // 清空表格数据
        for (Book book : books) {
            tableModel.addRow(new Object[]{book.getTitle(), book.getUsername(), book.getBorrowingDate()});
        }
    }

    private void loadBorrowings() {
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
        searchField.setText("");
    }
}
