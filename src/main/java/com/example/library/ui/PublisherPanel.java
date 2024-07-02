package com.example.library.ui;

import com.example.library.dao.PublisherManager;
import com.example.library.model.Publisher;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.List;

public class PublisherPanel extends JPanel {
    private Connection connection;
    private PublisherManager publisherManager;

    private JPanel inputPanel;
    private JTextField nameField;
    private JTextField addressField;
    private JTextField searchField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton searchButton;
    private JTable publisherTable;
    private DefaultTableModel tableModel;

    public PublisherPanel(Connection connection) {
        this.connection = connection;
        this.publisherManager = new PublisherManager(connection);

        initComponents();
        addListeners();
        loadPublishers();
    }

    private void initComponents() {
        inputPanel = new JPanel();
        GroupLayout layout = new GroupLayout(inputPanel);
        inputPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel nameLabel = new JLabel("出版社名称:");
        nameField = new JTextField(15); // 设置文本框的首选宽度
        JLabel addressLabel = new JLabel("地址:");
        addressField = new JTextField(15); // 设置文本框的首选宽度
        JLabel searchLabel = new JLabel("查询出版社:");
        searchField = new JTextField(15); // 设置文本框的首选宽度

        addButton = new JButton("添加出版社");
        deleteButton = new JButton("删除出版社");
        updateButton = new JButton("更新出版社");
        searchButton = new JButton("查询出版社");

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(nameLabel)
                                .addComponent(addressLabel)
                                .addComponent(searchLabel))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(nameField)
                                .addComponent(addressField)
                                .addComponent(searchField)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(addButton)
                                        .addComponent(deleteButton)
                                        .addComponent(updateButton)
                                        .addComponent(searchButton)))
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(nameLabel)
                                .addComponent(nameField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(addressLabel)
                                .addComponent(addressField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(searchLabel)
                                .addComponent(searchField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(addButton)
                                .addComponent(deleteButton)
                                .addComponent(updateButton)
                                .addComponent(searchButton))
        );

        tableModel = new DefaultTableModel(new Object[]{"ID", "出版社名称", "地址"}, 0);
        publisherTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(publisherTable);

        setLayout(new BorderLayout(10, 10));
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addListeners() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPublisher();
                loadPublishers(); // 重新加载出版社列表
                clearTextFields(); // 清空文本框
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePublisher();
                loadPublishers(); // 重新加载出版社列表
                clearTextFields(); // 清空文本框
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePublisher();
                loadPublishers(); // 重新加载出版社列表
                clearTextFields(); // 清空文本框
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchPublisher();
            }
        });

        publisherTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = publisherTable.getSelectedRow();
                if (selectedRow >= 0) {
                    nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    addressField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                } else {
                    clearTextFields(); // 清空文本框
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (publisherTable.rowAtPoint(e.getPoint()) == -1) {
                    clearTextFields();
                }
            }
        });
    }

    private void addPublisher() {
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        // 检查必填字段是否为空
        if (name.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入出版社名称和地址", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Publisher publisher = new Publisher(name, address);
        publisherManager.addPublisher(publisher);
        JOptionPane.showMessageDialog(this, "出版社添加成功");
    }

    private void deletePublisher() {
        int selectedRow = publisherTable.getSelectedRow();
        if (selectedRow >= 0) {
            int publisherId = (int) tableModel.getValueAt(selectedRow, 0);
            publisherManager.deletePublisher(publisherId);
            JOptionPane.showMessageDialog(this, "出版社删除成功");
        } else {
            JOptionPane.showMessageDialog(this, "请选择要删除的出版社", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePublisher() {
        int selectedRow = publisherTable.getSelectedRow();
        if (selectedRow >= 0) {
            int publisherId = (int) tableModel.getValueAt(selectedRow, 0);
            String name = nameField.getText().trim();
            String address = addressField.getText().trim();

            if (name.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入出版社名称和地址", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Publisher publisher = new Publisher(publisherId, name, address);
            publisherManager.updatePublisher(publisher);
            JOptionPane.showMessageDialog(this, "出版社信息更新成功");
        } else {
            JOptionPane.showMessageDialog(this, "请选择要更新的出版社", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchPublisher() {
        String name = searchField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入要查询的出版社名称", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Publisher publisher = publisherManager.getPublisherByName(name);
        if (publisher != null) {
            tableModel.setRowCount(0); // 清空表格数据
            tableModel.addRow(new Object[]{publisher.getId(), publisher.getName(), publisher.getAddress()});
        } else {
            JOptionPane.showMessageDialog(this, "未找到指定的出版社", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadPublishers() {
        List<Publisher> publishers = publisherManager.getAllPublishers();
        tableModel.setRowCount(0); // 清空表格数据
        for (Publisher publisher : publishers) {
            tableModel.addRow(new Object[]{publisher.getId(), publisher.getName(), publisher.getAddress()});
        }
    }

    private void clearTextFields() {
        nameField.setText("");
        addressField.setText("");
        searchField.setText("");
    }
}
