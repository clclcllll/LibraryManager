package com.example.library.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class AdminPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public AdminPanel(Connection connection) {
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JMenuBar adminMenuBar = new JMenuBar();

        JMenuItem bookMenuItem = new JMenuItem("图书管理");
        adminMenuBar.add(bookMenuItem);

        JMenuItem publisherMenuItem = new JMenuItem("出版社管理");
        adminMenuBar.add(publisherMenuItem);

        JMenuItem reservationMenuItem = new JMenuItem("预定管理");
        adminMenuBar.add(reservationMenuItem);

        JMenuItem borrowingMenuItem = new JMenuItem("借阅管理");
        adminMenuBar.add(borrowingMenuItem);

        //设置退出放回到登录
        JMenuItem logoutMenuItem = new JMenuItem("退出");
        adminMenuBar.add(logoutMenuItem);


        //设置导航栏边框
        adminMenuBar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        //设置导航项边框
        bookMenuItem.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        publisherMenuItem.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        reservationMenuItem.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        borrowingMenuItem.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        logoutMenuItem.setBorder(BorderFactory.createLineBorder(Color.red));


        //设置背景图片
        adminMenuBar.setBackground(Color.YELLOW);


        //为导航栏添加事件监听
        bookMenuItem.addActionListener(e -> cardLayout.show(cardPanel, "bookPanel"));
        publisherMenuItem.addActionListener(e -> cardLayout.show(cardPanel, "publisherPanel"));
        reservationMenuItem.addActionListener(e -> cardLayout.show(cardPanel, "reservationPanel"));
        borrowingMenuItem.addActionListener(e -> cardLayout.show(cardPanel, "borrowingPanel"));
        logoutMenuItem.addActionListener(e -> {
            MainFrame.mainCardLayout.show(MainFrame.mainCardPanel, "loginPanel");
        });

        add(adminMenuBar, BorderLayout.NORTH);

        cardPanel.add(new BookPanel(connection), "bookPanel");
        cardPanel.add(new PublisherPanel(connection), "publisherPanel");
        cardPanel.add(new ReservationPanel(connection), "reservationPanel");
        cardPanel.add(new BorrowingPanel(connection), "borrowingPanel");

        add(cardPanel, BorderLayout.CENTER);
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public JPanel getCardPanel() {
        return cardPanel;
    }
}
