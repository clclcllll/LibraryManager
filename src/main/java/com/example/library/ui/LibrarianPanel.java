package com.example.library.ui;

import com.example.library.dao.UserManager;
import com.example.library.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

public class LibrarianPanel extends JPanel {
    private Connection connection;
    private UserManager userManager;

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton viewAdminsButton;
    private JButton viewStudentsButton;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton exitButton;
    public LibrarianPanel(Connection connection) {
        this.connection = connection;
        this.userManager = new UserManager(connection);

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

        JLabel usernameLabel = new JLabel("账号名:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("密码:");
        passwordField = new JPasswordField();
        JLabel roleLabel = new JLabel("角色:");
        roleComboBox = new JComboBox<>(new String[]{"admin", "user"});

        addButton = new JButton("添加用户");
        deleteButton = new JButton("删除用户");
        updateButton = new JButton("更新用户");
        viewAdminsButton = new JButton("查看管理员");
        viewStudentsButton = new JButton("查看用户");
        exitButton = new JButton("退出");
        exitButton.setBorder(BorderFactory.createLineBorder(Color.red));


        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(usernameLabel)
                                        .addComponent(passwordLabel)
                                        .addComponent(roleLabel))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(usernameField)
                                        .addComponent(passwordField)
                                        .addComponent(roleComboBox)))
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(addButton)
                                .addComponent(deleteButton)
                                .addComponent(updateButton)
                                .addComponent(viewAdminsButton)
                                .addComponent(viewStudentsButton)
                                .addComponent(exitButton))
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(usernameLabel)
                                .addComponent(usernameField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(passwordLabel)
                                .addComponent(passwordField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(roleLabel)
                                .addComponent(roleComboBox))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(addButton)
                                .addComponent(deleteButton)
                                .addComponent(updateButton)
                                .addComponent(viewAdminsButton)
                                .addComponent(viewStudentsButton)
                                .addComponent(exitButton))
        );

        tableModel = new DefaultTableModel(new Object[]{"用户名", "角色"}, 0);
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);

        //将导航栏放在右上角
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

    }

    private void addListeners() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addUser();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteUser();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUser();
            }
        });

        viewAdminsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAdmins();
            }
        });

        viewStudentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewStudents();
            }
        });
        //退出
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.mainCardLayout.show(MainFrame.mainCardPanel, "loginPanel");
            }
        });
    }

    private void addUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String role = (String) roleComboBox.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "用户名和密码不能为空", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = new User(username, password, role);
        if (userManager.getUser(username, password,role) != null) {
            JOptionPane.showMessageDialog(this, "用户已存在", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        userManager.addUser(user);
        JOptionPane.showMessageDialog(this, "用户添加成功");
        clearTextFields();
        loadUsers();
    }

    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            String username = (String) tableModel.getValueAt(selectedRow, 0);
            userManager.deleteUser(username);
            JOptionPane.showMessageDialog(this, "用户删除成功");
            loadUsers();
        } else {
            JOptionPane.showMessageDialog(this, "请选择要删除的用户", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            String username = (String) tableModel.getValueAt(selectedRow, 0);
            String password = new String(passwordField.getPassword()).trim();
            String role = (String) roleComboBox.getSelectedItem();

            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "密码不能为空", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User user = new User(username, password, role);
            userManager.updateUser(user);
            JOptionPane.showMessageDialog(this, "用户更新成功");
            loadUsers();
        } else {
            JOptionPane.showMessageDialog(this, "请选择要更新的用户", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewAdmins() {
        loadUsersByRole("admin");
    }

    private void viewStudents() {
        loadUsersByRole("user");
    }

    private void loadUsers() {
        List<User> users = userManager.getAllUsers();
        tableModel.setRowCount(0); // 清空表格数据
        for (User user : users) {
            tableModel.addRow(new Object[]{user.getUsername(), user.getRole()});
        }
    }

    private void loadUsersByRole(String role) {
        List<User> users = userManager.getAllUsers();
        tableModel.setRowCount(0); // 清空表格数据
        for (User user : users) {
            if (user.getRole().equals(role)) {
                tableModel.addRow(new Object[]{user.getUsername(), user.getRole()});
            }
        }
    }

    private void clearTextFields() {
        usernameField.setText("");
        passwordField.setText("");
    }
}
