package com.example.library.ui;

import com.example.library.dao.UserManager;
import com.example.library.model.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class LoginPanel extends JPanel {
    private Connection connection;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private MainFrame mainFrame;

    private UserManager userManager;
    private BufferedImage backgroundImage;


    public LoginPanel(MainFrame mainFrame, Connection connection) {
        this.connection = connection;
        userManager= new UserManager(connection);
        this.mainFrame = mainFrame;

        loadBackgroundImage(); // 加载背景图片
        initComponents();
    }
    // 加载背景图片
    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("src/main/resources/images/backgroundimage.jpg")); // 替换为您的背景图片文件路径
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // 绘制背景图片
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 20, 10); // 设置组件间距

        // 创建欢迎消息的 JLabel，并设置字体和颜色
        JLabel welcomeLabel = new JLabel("欢迎登录图书管理系统");
        // 设置字体
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 35));
        welcomeLabel.setForeground(Color.BLACK); // 设置字体颜色
        gbc.gridwidth = 2; // 设置组件跨越两列
        gbc.anchor = GridBagConstraints.CENTER; // 设置组件居中显示
        add(welcomeLabel, gbc);

        gbc.gridy++;
        JLabel usernameLabel = new JLabel("用户名:");
        // 设置字体和大小
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(usernameLabel, gbc);

        gbc.gridy++;
        usernameField = new JTextField(30);
        add(usernameField, gbc);

        gbc.gridy++;
        JLabel passwordLabel = new JLabel("密码:");
        // 设置字体和大小
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(passwordLabel, gbc);

        gbc.gridy++;
        passwordField = new JPasswordField(30);
        add(passwordField, gbc);

        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER; // 设置组件居中显示
        loginButton = new JButton("登录");
        //设置按钮大小
        loginButton.setPreferredSize(new Dimension(200, 30));

        //设置按钮背景颜色
        loginButton.setBackground(Color.BLUE);

        add(loginButton, gbc);


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
    }

    private void login() {
        //如果用户有没输入的文本框
        if (usernameField.getText().trim().isEmpty() || new String(passwordField.getPassword()).trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "请输入用户名和密码", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        User user = userManager.getUser(username,password);
        if (user != null) {
            JOptionPane.showMessageDialog(this, "登录成功");
            refresh();
            mainFrame.showPanel(user.getRole().equals("admin") ? "adminPanel" :
                    user.getRole().equals("librarian") ? "librarianPanel" : "userPanel");
            mainFrame.setUser(user);
        } else {
            refresh();
            JOptionPane.showMessageDialog(this, "用户名或密码错误", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    //刷新
    public void refresh() {
        usernameField.setText("");
        passwordField.setText("");
    }




}
