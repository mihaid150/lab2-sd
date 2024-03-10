package com.example.lab2.repository;

import com.example.lab2.database.DatabaseConnectionFactory;
import com.example.lab2.database.DbConnection;
import com.example.lab2.database.SupportedDatabase;
import com.example.lab2.model.Book;
import com.example.lab2.model.BookBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookRepositorySQLTest {

    private BookRepository bookRepository;

    @BeforeAll
    public void setupClass() {
        DbConnection connectionWrapper = DatabaseConnectionFactory.getConnectionWrapper(SupportedDatabase.MYSQL, true);
        bookRepository = new BookRepositorySQL(connectionWrapper.getConnection());
    }

    @BeforeEach
    void setup() {
        bookRepository.removeAll();
    }

    @Test
    void findAll() {
        assertEquals(0, bookRepository.findAll().size());
    }

    @Test
    void findById() {
        Book expectedBook = new BookBuilder()
                .setAuthor("author")
                .setTitle("title")
                .setPublishedDate(LocalDate.now())
                .build();
        assertTrue(bookRepository.create(expectedBook));

        long expectedBookId = bookRepository.findBookIdByUniqueAttributes("title", "author");
        Optional<Book> optionalActualBook = bookRepository.findById(expectedBookId);
        assertTrue(optionalActualBook.isPresent());
        Book actualBook = optionalActualBook.get();
        assertEquals(expectedBook, actualBook);
    }

    @Test
    void createNoAuthor() {
        Book bookNoAuthor = new BookBuilder()
                .setTitle("title")
                .setPublishedDate(LocalDate.now())
                .build();
        assertFalse(bookRepository.create(bookNoAuthor));
    }

    @Test
    void createNoTitle() {
        Book bookNoTitle = new BookBuilder()
                .setAuthor("author")
                .setPublishedDate(LocalDate.now())
                .build();
        assertFalse(bookRepository.create(bookNoTitle));
    }

    @Test
    void createValidBook() {
        Book validBook = new BookBuilder()
                .setAuthor("author")
                .setTitle("title")
                .setPublishedDate(LocalDate.now())
                .build();
        assertTrue(bookRepository.create(validBook));
    }

    @Test
    void removeAll() {
        bookRepository.create(new BookBuilder()
                .setAuthor("author")
                .setTitle("title")
                .setPublishedDate(LocalDate.now())
                .build());
        bookRepository.removeAll();
        assertEquals(0, bookRepository.findAll().size());
    }
}