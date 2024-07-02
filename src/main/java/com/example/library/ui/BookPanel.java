package com.example.library.ui;


import com.example.library.dao.BookManager;
import com.example.library.dao.PublisherManager;
import com.example.library.model.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class BookPanel extends JPanel {
    private Connection connection;
    private BookManager bookManager;
    private PublisherManager publisherManager;

    private JPanel panel;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField isbnField;
    private JTextField publisherField;
    private JTextField searchField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton searchButton;
    private JTable bookTable;
    private DefaultTableModel tableModel;

    public BookPanel(Connection connection) {
        this.connection = connection;
        this.bookManager = new BookManager(connection);
        this.publisherManager = new PublisherManager(connection);

        initComponents();
        addListeners();
        loadBooks();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel titleLabel = new JLabel("书籍标题:");
        titleField = new JTextField();
        JLabel authorLabel = new JLabel("作者:");
        authorField = new JTextField();
        JLabel isbnLabel = new JLabel("ISBN:");
        isbnField = new JTextField();
        JLabel publisherLabel = new JLabel("出版社名称:");
        publisherField = new JTextField();
        JLabel searchLabel = new JLabel("查询书籍:");
        searchField = new JTextField();

        addButton = new JButton("添加书籍");
        deleteButton = new JButton("删除书籍");
        updateButton = new JButton("更新书籍");
        searchButton = new JButton("查询书籍");

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(titleLabel)
                                        .addComponent(authorLabel)
                                        .addComponent(isbnLabel)
                                        .addComponent(publisherLabel)
                                        .addComponent(searchLabel))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(titleField)
                                        .addComponent(authorField)
                                        .addComponent(isbnField)
                                        .addComponent(publisherField)
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
                                .addComponent(titleLabel)
                                .addComponent(titleField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(authorLabel)
                                .addComponent(authorField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(isbnLabel)
                                .addComponent(isbnField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(publisherLabel)
                                .addComponent(publisherField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(searchLabel)
                                .addComponent(searchField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(addButton)
                                .addComponent(deleteButton)
                                .addComponent(updateButton)
                                .addComponent(searchButton))
        );

        tableModel = new DefaultTableModel(new Object[]{"ID", "书籍标题", "作者", "ISBN", "出版社名称"}, 0);
        bookTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);

        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addListeners() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addBook();
                } catch (SQLIntegrityConstraintViolationException ex) {
                    JOptionPane.showMessageDialog(BookPanel.this, "书籍已存在", "错误", JOptionPane.ERROR_MESSAGE);
                }
                loadBooks(); // 重新加载书籍列表
                clearTextFields(); // 清空文本框
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBook();
                loadBooks(); // 重新加载书籍列表
                clearTextFields(); // 清空文本框
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBook();
                loadBooks(); // 重新加载书籍列表
                clearTextFields(); // 清空文本框
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBook();
            }
        });

        // 鼠标点击表格事件
        bookTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = bookTable.getSelectedRow();
                if (selectedRow >= 0) {
                    titleField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    authorField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    isbnField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    publisherField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                } else {
                    clearTextFields(); // 清空文本框
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (bookTable.rowAtPoint(e.getPoint()) == -1) {
                    clearTextFields();
                }
            }
        });
    }

    private void addBook() throws SQLIntegrityConstraintViolationException {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String isbn = isbnField.getText().trim();
        String publisher = publisherField.getText().trim();
        // 检查必填字段是否为空
        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() || publisher.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入书籍标题、作者、ISBN和出版社名称", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 检查出版社是否存在
        if (!publisherManager.isPublisherExists(publisher)) {
            JOptionPane.showMessageDialog(this, "没有合作的出版社", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Book book = new Book(title, author, isbn, publisher);
        bookManager.addBook(book);
    }

    private void deleteBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow >= 0) {
            int bookId = (int) tableModel.getValueAt(selectedRow, 0);
            bookManager.deleteBook(bookId);
        } else {
            JOptionPane.showMessageDialog(this, "请选择要删除的书籍", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow >= 0) {
            int bookId = (int) tableModel.getValueAt(selectedRow, 0);
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String isbn = isbnField.getText().trim();
            String publisher = publisherField.getText().trim();
            // 检查必填字段是否为空
            if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() || publisher.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入书籍标题、作者、ISBN和出版社名称", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 检查出版社是否存在
            if (!publisherManager.isPublisherExists(publisher)) {
                JOptionPane.showMessageDialog(this, "没有合作的出版社", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Book book = new Book(bookId, title, author, isbn, publisher);

            bookManager.updateBook(book);
        } else {
            JOptionPane.showMessageDialog(this, "请选择要更新的书籍", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchBook() {
        String search = searchField.getText().trim();
        if (search.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入查询内容", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Book book = bookManager.getBookByTitle(search);
        if (book == null) {
            JOptionPane.showMessageDialog(this, "未找到书籍", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            tableModel.setRowCount(0); // 清空表格数据
            tableModel.addRow(new Object[]{book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn(), book.getPublisherName()});
        }
    }

    private void loadBooks() {
        List<Book> books = bookManager.getAllBooks();
        tableModel.setRowCount(0); // 清空表格数据
        for (Book book : books) {
            tableModel.addRow(new Object[]{book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn(), book.getPublisherName()});
        }
    }

    private void clearTextFields() {
        titleField.setText("");
        authorField.setText("");
        isbnField.setText("");
        publisherField.setText("");
    }
}
