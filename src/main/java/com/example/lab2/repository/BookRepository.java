package com.example.lab2.repository;

import com.example.lab2.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> findAll();
    Optional<Book> findById(Long id);
    boolean create(Book book);
    void removeAll();
}
