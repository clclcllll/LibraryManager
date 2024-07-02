package com.example.library.ui;

import com.example.library.model.User;
import com.example.library.ui.AdminPanel;
import com.example.library.ui.LoginPanel;
import com.example.library.ui.UserPanel;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class MainFrame extends JFrame {
    public static CardLayout mainCardLayout;
    public static   JPanel mainCardPanel;
    private User currentUser;
    private JMenuBar adminMenuBar;
    private JMenuBar userMenuBar;
    private Connection connection;

    public MainFrame(Connection connection) {
        this.connection = connection;
        initComponents();

    }

    private void initComponents() {
        mainCardLayout = new CardLayout();
        mainCardPanel = new JPanel(mainCardLayout);

        // 初始化面板

        LoginPanel loginPanel = new LoginPanel(this, this.connection);
        AdminPanel adminPanel = new AdminPanel(connection);
        UserPanel userPanel = new UserPanel(connection);
        LibrarianPanel librarianPanel = new LibrarianPanel(connection);

        // 添加面板到卡片布局
        mainCardPanel.add(loginPanel, "loginPanel");
        mainCardPanel.add(adminPanel, "adminPanel");
        mainCardPanel.add(librarianPanel, "librarianPanel");
        mainCardPanel.add(userPanel, "userPanel");

        setLayout(new BorderLayout());
        add(mainCardPanel,BorderLayout.CENTER);

        setTitle("图书馆管理系统");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public void setUser(User user) {
        this.currentUser = user;

    }
    private void showAdminPanel(String panelName) {
        ((CardLayout)((AdminPanel)mainCardPanel.getComponent(1)).getCardLayout()).show(((AdminPanel)mainCardPanel.getComponent(1)).getCardPanel(), panelName);
    }
    private void showUserPanel(String panelName) {
        ((CardLayout)((UserPanel)mainCardPanel.getComponent(2)).getCardLayout()).show(((UserPanel)mainCardPanel.getComponent(2)).getCardPanel(), panelName);
    }
    public void showPanel(String panelName) {
        mainCardLayout.show( mainCardPanel, panelName);
    }

}
