package com.example.lab2.repository.Book;

import com.example.lab2.model.book.Book;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BookRepository {

    List<Book> findAll();

    Optional<Book> findById(Long id);

    Book create(Book book) throws SQLException;

    void removeAll();

}
