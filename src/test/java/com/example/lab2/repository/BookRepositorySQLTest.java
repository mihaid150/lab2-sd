package com.example.lab2.repository;

import com.example.lab2.database.DatabaseConnectionFactory;
import com.example.lab2.database.DbConnection;
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
import java.util.Optional;

import static com.example.lab2.database.SupportedDatabase.MYSQL;
import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookRepositorySQLTest {

    private BookRepository repository;

    @BeforeAll
    public void setupClass() {
        DbConnection connectionWrapper = DatabaseConnectionFactory.getConnectionWrapper(MYSQL, true);
        repository = new BookRepositorySQL(connectionWrapper.getConnection());
    }

    @BeforeEach
    void setup() {
        repository.removeAll();
    }

    @Test
    void findAll() {
        assertEquals(0, repository.findAll().size());
    }

    @Test
    void findById() {

    }

    @Test
    void create() throws SQLException {
        Book bookNoAuthor = new BookBuilder()
                .setTitle("title")
                .setPublishedDate(LocalDate.now())
                .build();

        assertThrows(SQLException.class, () -> repository.create(bookNoAuthor));

        Book bookNoTitle = new BookBuilder().setAuthor("author")
                .setPublishedDate(LocalDate.now())
                .build();

        assertThrows(SQLException.class, () -> repository.create(bookNoTitle));


        Book validBook = new BookBuilder()
                .setAuthor("author")
                .setTitle("title")
                .setPublishedDate(LocalDate.now())
                .build();

        assertNotNull(repository.create(validBook));
    }

    @Test
    void removeAll() throws SQLException {
        repository.create(new BookBuilder()
                .setAuthor("author")
                .setTitle("title")
                .setPublishedDate(LocalDate.now())
                .build());

        repository.removeAll();

        assertEquals(0, repository.findAll().size());
    }
}