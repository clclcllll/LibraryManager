package com.example.library;

import com.example.library.dao.BookManager;
import com.example.library.ui.MainFrame;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            // 获取与数据库的连接
            Connection connection = DatabaseConnection.getConnection();

            // 启动图书馆管理系统的主界面
            new MainFrame(connection);
        } catch (SQLException ex) {
            ex.printStackTrace();
            // 处理连接数据库失败的情况
            JOptionPane.showMessageDialog(null, "连接数据库失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}
