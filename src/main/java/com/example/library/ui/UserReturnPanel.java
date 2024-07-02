package com.example.library.ui;

import com.example.library.dao.BorrowingManager;
import com.example.library.model.Book;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class UserReturnPanel extends JPanel {
    private Connection connection;
    private BorrowingManager borrowingManager;

    private JTextField bookTitleField;
    private JTextField usernameField;
    private JButton returnButton;

    public UserReturnPanel(Connection connection) {
        this.connection = connection;
        this.borrowingManager = new BorrowingManager(connection);

        initComponents();
        addListeners();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        // 设置标题
        JLabel titleLabel = new JLabel("归还书籍", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);



        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;

        JLabel bookTitleLabel = new JLabel("书籍名称:");
        inputPanel.add(bookTitleLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        bookTitleField = new JTextField(20);
        inputPanel.add(bookTitleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel usernameLabel = new JLabel("用户名:");
        inputPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        usernameField = new JTextField(20);
        inputPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        returnButton = new JButton("归还书籍");

        inputPanel.add(returnButton, gbc);


        add(inputPanel, BorderLayout.CENTER);
    }

    private void addListeners() {
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnBook();
            }
        });
    }

    private void returnBook() {
        try {
            String bookTitle = bookTitleField.getText().trim();
            String username = usernameField.getText().trim();

            if (bookTitle.isEmpty() || username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "所有字段都是必填项", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Book book = new Book(bookTitle, username);
            borrowingManager.returnBorrowing(book);
            JOptionPane.showMessageDialog(this, "书籍归还成功，谢谢！");
            clearTextFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "输入格式有误，请检查输入内容", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearTextFields() {
        bookTitleField.setText("");
        usernameField.setText("");
    }
}
