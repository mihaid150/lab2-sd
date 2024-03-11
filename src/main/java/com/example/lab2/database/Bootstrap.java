package com.example.lab2.database;

import com.example.lab2.model.security.ERole;
import com.example.lab2.repository.Security.RoleRepository;
import com.example.lab2.repository.Security.RoleRepositorySQL;
import com.example.lab2.repository.Security.UserRepository;
import com.example.lab2.repository.Security.UserRepositorySQL;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import static com.example.lab2.database.Constants.SCHEMAS.SCHEMAS;

public class Bootstrap {
    private RoleRepository roleRepository;
    private UserRepository userRepository;

    public void bootstrap() throws SQLException {
        dropAll();
        createTables();
        createUserData();
    }

    private void dropAll() throws SQLException {
        for (String schema : SCHEMAS) {
            System.out.println("Dropping all tables in schema: " + schema);

            Connection connection = new JDBCConnectionWrapper(schema).getConnection();
            Statement statement = connection.createStatement();

            String[] dropStatements = {
                    "TRUNCATE `user_role`;",
                    "DROP TABLE IF EXISTS `user_role`;",
                    "TRUNCATE `role`;",
                    "DROP TABLE IF EXISTS `book`, `role`, `user`;"
            };

            Arrays.stream(dropStatements).forEach(dropStatement -> {
                try {
                    statement.execute(dropStatement);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void createTables() throws SQLException {
        SQLTableCreationFactory sqlTableCreationFactory = new SQLTableCreationFactory();

        for (String schema : SCHEMAS) {
            System.out.println("Bootstrapping " + schema + " schema");

            JDBCConnectionWrapper connectionWrapper = new JDBCConnectionWrapper(schema);
            Connection connection = connectionWrapper.getConnection();

            Statement statement = connection.createStatement();

            for (String table : Constants.TABLES.ORDERED_TABLES_FOR_CREATION) {
                String createTableSQL = sqlTableCreationFactory.getCreateSQLForTable(table);
                statement.execute(createTableSQL);
            }
        }
    }

    private void createUserData() {
        for (String schema : SCHEMAS) {
            System.out.println("Bootstrapping user data for " + schema);

            createRoles(schema);
            createUsers(schema);
        }
    }

    private void createRoles(String schema) {
        JDBCConnectionWrapper connectionWrapper = new JDBCConnectionWrapper(schema);
        roleRepository = new RoleRepositorySQL(connectionWrapper.getConnection());
        for (ERole role : ERole.values()) {
            roleRepository.create(role);
        }
    }

    private void createUsers(String schema) {
        JDBCConnectionWrapper connectionWrapper = new JDBCConnectionWrapper(schema);
        roleRepository = new RoleRepositorySQL(connectionWrapper.getConnection());
        userRepository = new UserRepositorySQL(connectionWrapper.getConnection(), roleRepository);
        // ...
    }
}
