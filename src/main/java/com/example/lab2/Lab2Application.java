package com.example.lab2;

import com.example.lab2.database.DatabaseConnectionFactory;
import com.example.lab2.database.DbConnection;
import com.example.lab2.database.SupportedDatabase;
import com.example.lab2.model.Book;
import com.example.lab2.model.BookBuilder;
import com.example.lab2.repository.BookRepository;
import com.example.lab2.repository.BookRepositoryCacheDecorator;
import com.example.lab2.repository.BookRepositorySQL;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;
import java.time.LocalDate;


@SpringBootApplication
public class Lab2Application {

    public static void main(String[] args) throws SQLException {
        DbConnection connectionWrapper = DatabaseConnectionFactory.getConnectionWrapper(SupportedDatabase.MYSQL, true);

        if(connectionWrapper.testConnection()) {
            System.out.println("Connection successful!");
        } else {
            System.out.println("Connection failed!");
        }

        Book book = new BookBuilder()
                .setAuthor("John Doe")
                .setTitle("Java for Dummies")
                .setPublishedDate(LocalDate.now())
                .build();

        BookRepositorySQL mysqlRepository = new BookRepositorySQL(connectionWrapper.getConnection());
        BookRepository bookRepository = new BookRepositoryCacheDecorator(mysqlRepository);
    }
}

