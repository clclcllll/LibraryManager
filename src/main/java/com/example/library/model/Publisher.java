package com.example.library.model;

public class Publisher {
    private int id;
    private String name;
    private String address;

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Publisher() {
    }

    public Publisher(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public Publisher( String name,int id) {
        this.id = id;
        this.name = name;
    }

    public Publisher(String name, String address) {
        this.name = name;
        this.address = address;
    }
}
