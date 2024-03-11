package com.example.lab2.service.book;

import com.example.lab2.database.DatabaseConnectionFactory;
import com.example.lab2.database.SupportedDatabase;
import com.example.lab2.model.book.Book;
import com.example.lab2.model.book.BookBuilder;
import com.example.lab2.repository.Book.BookRepository;
import com.example.lab2.repository.Book.BookRepositorySQL;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookServiceTest {

    private BookService bookService;
    private BookRepository bookRepository;

    @BeforeEach
    public void cleanup() {
        bookRepository.removeAll();
    }

    @BeforeAll
    public void setup() {
        bookRepository = new BookRepositorySQL(DatabaseConnectionFactory
                .getConnectionWrapper(SupportedDatabase.MYSQL, true).getConnection());
        bookService = new BookService(bookRepository);
    }

    @Test
    void findAllNull() {
        assertEquals(0, bookService.findAll().size());
    }

    @Test
    void findAll() throws SQLException {
        Book book = new BookBuilder()
                .setAuthor("Liviu Rebreanu")
                .setTitle("Ion")
                .setPublishedDate(LocalDate.now().withYear(1920))
                .build();
        bookService.create(book);
        assertEquals(1, bookService.findAll().size());
    }

    @Test
    void findById() throws SQLException {
        Book book = new BookBuilder()
                .setAuthor("Liviu Rebreanu")
                .setTitle("Ion")
                .setPublishedDate(LocalDate.now().withYear(1920))
                .build();
        Book actualBook = bookService.create(book);
        assertEquals(book, bookService.findById(actualBook.getId()));
    }

    @Test
    void create() throws SQLException {
        Book book = new BookBuilder()
                .setAuthor("Liviu Rebreanu")
                .setTitle("Ion")
                .setPublishedDate(LocalDate.now().withYear(1920))
                .build();
        Book actualBook = bookService.create(book);
        assertNotNull(actualBook);
    }

    @Test
    void createException() throws SQLException {
        Book book = new BookBuilder().build();
        assertThrows(NullPointerException.class, () -> bookService.create(book));
    }

    @Test
    void getAgeOfBook() throws SQLException {
        int publishedYearOfIon = 1920;
        Book book = new BookBuilder()
                .setAuthor("Liviu Rebreanu")
                .setTitle("Ion")
                .setPublishedDate(LocalDate.now().withYear(publishedYearOfIon))
                .build();

        Book savedBook = bookService.create(book);
        int actual = bookService.getAgeOfBook(savedBook.getId());
        assertEquals(LocalDate.now().getYear() - publishedYearOfIon, actual);
    }

    @Test
    void getAgeOfBookNotFoundBook() {
        long bookId = 10L;
        assertThrows(IllegalArgumentException.class, () -> bookService.getAgeOfBook(bookId));
    }
}