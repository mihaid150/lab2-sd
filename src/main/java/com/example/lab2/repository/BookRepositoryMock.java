package com.example.lab2.repository;

import com.example.lab2.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMock implements BookRepository{

    private final List<Book> books;

    public BookRepositoryMock() {
        books = new ArrayList<>();
    }

    @Override
    public List<Book> findAll() {
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        return books
                .stream()
                .filter(book -> book.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean create(Book book) {
        return books.add(book);
    }

    @Override
    public long findBookIdByUniqueAttributes(String title, String author) {
        return books
                .stream()
                .filter(book -> book.getAuthor().equals(author) && book.getTitle().equals(title))
                .findFirst()
                .map(Book::getId)
                .orElse(-1L);
    }

    @Override
    public void removeAll() {
        books.clear();
    }
}
