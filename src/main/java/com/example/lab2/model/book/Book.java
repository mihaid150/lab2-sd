package com.example.lab2.model.book;

import java.time.LocalDate;
import java.util.Objects;

public class Book {

    private Long id;
    private String author;
    private String title;
    private LocalDate publishedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public int getAge() {
        int yearOfPublishing = publishedDate.getYear();
        int yearToday = LocalDate.now().getYear();
        return yearToday - yearOfPublishing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) && Objects.equals(author, book.author) && Objects.equals(title, book.title) && Objects.equals(publishedDate, book.publishedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, title, publishedDate);
    }
}