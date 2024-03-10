package com.example.lab2.repository;

public abstract class BookRepositoryDecorator implements BookRepository {
    protected BookRepository decoratedRepository;

    public BookRepositoryDecorator(BookRepository bookRepository) {
        this.decoratedRepository = bookRepository;
    }
}
