package com.example.library.dao;

import com.example.library.model.Publisher;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PublisherManager {
    private final Connection connection;

    public PublisherManager(Connection connection) {
        this.connection = connection;
    }

    public void addPublisher(Publisher publisher) {
        String sql = "INSERT INTO publishers (name, address) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, publisher.getName());
            statement.setString(2, publisher.getAddress());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePublisher(int id) {
        String sql = "DELETE FROM publishers WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePublisher(Publisher publisher) {
        String sql = "UPDATE publishers SET name = ?, address = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, publisher.getName());
            statement.setString(2, publisher.getAddress());
            statement.setInt(3, publisher.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Publisher> getAllPublishers() {
        List<Publisher> publishers = new ArrayList<>();
        String sql = "SELECT * FROM publishers";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Publisher publisher = new Publisher();
                publisher.setId(resultSet.getInt("id"));
                publisher.setName(resultSet.getString("name"));
                publisher.setAddress(resultSet.getString("address"));
                publishers.add(publisher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return publishers;
    }

    public Publisher getPublisherByName(String name) {
        String sql = "SELECT * FROM publishers WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Publisher publisher = new Publisher();
                publisher.setId(resultSet.getInt("id"));
                publisher.setName(resultSet.getString("name"));
                publisher.setAddress(resultSet.getString("address"));
                return publisher;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean isPublisherExists(String publisherName) {
        String sql = "SELECT COUNT(*) FROM publishers WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, publisherName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
