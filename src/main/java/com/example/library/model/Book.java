package com.example.library.model;

import java.sql.Date;

public class Book {
    private int reservationid;
    private int id;
    private String title;
    private String author;
    private String isbn;
    private String publisher;

    private String username;

    private Date reservationDate;
    private Date borrowingDate;

    // 构造函数
    public Book(int id, String title, String author, String isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }


    public Book(String title, String author, String isbn,String publisher) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publisher=publisher;
    }
    public Book(String title, String username, Date borrowingDate) {
        this.title = title;
        this.username = username;
        this.borrowingDate = borrowingDate;
    }
    public Book(String title,String username){
        this.title=title;
        this.username=username;
    }

    // 空构造函数
    public Book() {}


    public Book(int bookId, String username, Date reservationDate) {
        this.id=bookId;
        this.title=username;
        this.reservationDate=reservationDate;
    }

    //构造函数
    public Book(int reservationId, int bookId, String username, String isbn) {
        this.reservationid=reservationId;
        this.id=bookId;
        this.username=username;
        this.isbn=isbn;
    }

    public Book(int bookId, String title, String author, String isbn, String publisher) {
        this.id = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
    }

    //getborrowingDate
    public Date getBorrowingDate() {
        return borrowingDate;
    }
    //get reservationDate


    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public void setPublisherName(String publisher) {
        this.publisher = publisher;
    }


    public void setBookId(int bookId) {
        this.id = bookId;
    }
    public String getPublisherName() {
        return publisher;
    }

    public int getBookId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username=username;
    }
    //
    public void setBorrowingDate(Date borrowingDate) {
        this.borrowingDate = borrowingDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }
    //
    public Date getReservationDate() {
        return reservationDate;
    }


}
