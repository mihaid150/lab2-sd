package com.example.lab2.repository;

import com.example.lab2.database.DatabaseConnectionFactory;
import com.example.lab2.database.DbConnection;
import com.example.lab2.database.SupportedDatabase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

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
    }

    @Test
    void create() {
    }

    @Test
    void removeAll() {
    }
}