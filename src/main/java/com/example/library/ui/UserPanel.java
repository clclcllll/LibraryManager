package com.example.library.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;

public class UserPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public UserPanel(Connection connection) {
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        //设置导航栏
        JMenuBar menuBar = new JMenuBar();
        JMenuItem borrowItem = new JMenuItem("借书");
        JMenuItem returnItem = new JMenuItem("还书");
        JMenuItem reservationItem = new JMenuItem("预约");
        JMenuItem logoutMenuItem = new JMenuItem("退出");
        //为导航栏添加事件监听
        borrowItem.addActionListener(e -> cardLayout.show(cardPanel, "userBorrowingPanel"));
        returnItem.addActionListener(e -> cardLayout.show(cardPanel, "returnPanel"));
        reservationItem.addActionListener(e -> cardLayout.show(cardPanel, "userReservationPanel"));
        logoutMenuItem.addActionListener(e -> {
            MainFrame.mainCardLayout.show(MainFrame.mainCardPanel, "loginPanel");
        });

        //为导航栏设置边框
        menuBar.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        //为导航项设置边框
        borrowItem.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        returnItem.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        reservationItem.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        logoutMenuItem.setBorder(BorderFactory.createLineBorder(Color.red));

        //设置背景颜色
        menuBar.setBackground(Color.GREEN);

        menuBar.add(borrowItem);
        menuBar.add(returnItem);
        menuBar.add(reservationItem);
        menuBar.add(logoutMenuItem);
        add(menuBar, BorderLayout.NORTH);

        cardPanel.add(new UserReservationPanel(connection), "userReservationPanel");
        cardPanel.add(new UserBorrowingPanel(connection), "userBorrowingPanel");
        cardPanel.add(new UserReturnPanel(connection), "returnPanel");

        add(cardPanel, BorderLayout.CENTER);
    }


    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public JPanel getCardPanel() {
        return cardPanel;
    }
}
