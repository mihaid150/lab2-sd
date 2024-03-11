package com.example.lab2.service.security;

import com.example.lab2.database.Bootstrap;
import com.example.lab2.database.DatabaseConnectionFactory;
import com.example.lab2.database.DbConnection;
import com.example.lab2.database.SupportedDatabase;
import com.example.lab2.model.security.ERole;
import com.example.lab2.model.security.User;
import com.example.lab2.repository.Security.RoleRepository;
import com.example.lab2.repository.Security.RoleRepositorySQL;
import com.example.lab2.repository.Security.UserRepository;
import com.example.lab2.repository.Security.UserRepositorySQL;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SecurityServiceTest {

    private SecurityService securityService;
    private UserRepository userRepository;

    @BeforeEach
    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    @BeforeAll
    public void setup() throws SQLException {
        DbConnection connectionWrapper = DatabaseConnectionFactory.getConnectionWrapper(SupportedDatabase.MYSQL, true);
        Connection connection = connectionWrapper.getConnection();

        RoleRepository roleRepositorySQL = new RoleRepositorySQL(connection);
        userRepository = new UserRepositorySQL(connection, roleRepositorySQL);
        securityService = new SecurityService(userRepository, roleRepositorySQL);
        new Bootstrap().bootstrap();
    }

    @Test
    void register() throws SQLException {
        User user1 = securityService.register("johndoe", "password");
        assertNotNull(user1);
        assertTrue(user1.getId() >= 0);
        assertThrows(SQLException.class, () -> securityService.register("johndoe", "anotherpassword"));
    }

    @Test
    void login() {
    }
}