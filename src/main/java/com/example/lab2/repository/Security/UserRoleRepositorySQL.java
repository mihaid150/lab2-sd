package com.example.lab2.repository.Security;

import com.example.lab2.model.security.UserRole;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.lab2.database.Constants.TABLES.USER_ROLE;

public class UserRoleRepositorySQL implements UserRoleRepository{

    private final Connection connection;

    public UserRoleRepositorySQL(Connection connection) {
        this.connection = connection;
    }


    @Override
    public void create(long userId, long roleId) {
        try {
            PreparedStatement insertStatement = connection.prepareStatement("INSERT IGNORE INTO " + USER_ROLE +
                    " values (null, ?, ?);");
            insertStatement.setLong(1, userId);
            insertStatement.setLong(2, roleId);
            insertStatement.executeQuery();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<List<UserRole>> findByUserId(long userId) {
        Statement statement;
        List<UserRole> userRoles = new ArrayList<>();
        try {
            statement = connection.createStatement();
            String fetchQuery = "SELECT * from " + USER_ROLE + " where user_id = ?;";
            ResultSet resultSet = statement.executeQuery(fetchQuery);
            while(resultSet.next()) {
                UserRole userRole = new UserRole(resultSet.getLong("id"),
                        resultSet.getLong("user_id"), resultSet.getLong("role_id"));
                userRoles.add(userRole);
            }

            if(!userRoles.isEmpty()) {
                return Optional.of(userRoles);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}
