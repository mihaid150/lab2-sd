package com.example.lab2.repository;

import com.example.lab2.model.Book;

import java.util.List;
import java.util.Optional;

public class BookRepositoryCacheDecorator extends BookRepositoryDecorator{

    private final Cache<Book> cache;

    public BookRepositoryCacheDecorator(BookRepository bookRepository) {
        super(bookRepository);
        cache = new Cache<>();
    }

    @Override
    public List<Book> findAll() {
        if(cache.hasResult()) {
            return cache.load();
        }
        List<Book> allBooks = decoratedRepository.findAll();
        cache.save(allBooks);
        return allBooks;
    }

    @Override
    public Optional<Book> findById(Long id) {
        if(cache.hasResult()) {
            return cache
                    .load()
                    .stream()
                    .filter(book -> book.getId().equals(id))
                    .findFirst();
        }
        final Optional<Book> result = decoratedRepository.findById(id);
        if (result.isPresent()) {
            cache.add(result.get());
            return result;
        }
        return Optional.empty();
    }

    @Override
    public boolean create(Book book) {
        boolean result = decoratedRepository.create(book);
        cache.add(book);
        return result;
    }

    @Override
    public long findBookIdByUniqueAttributes(String title, String author) {
        if(cache.hasResult()) {
            return cache
                    .load()
                    .stream()
                    .filter(book -> book.getAuthor().equals(author) && book.getTitle().equals(title))
                    .findFirst()
                    .map(Book::getId)
                    .orElse(-1L);
        }
        return -1;
    }

    @Override
    public void removeAll() {
        decoratedRepository.removeAll();
        cache.invalidateCache();
    }
}
