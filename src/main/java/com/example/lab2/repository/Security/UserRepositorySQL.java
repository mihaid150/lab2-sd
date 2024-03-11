package com.example.lab2.repository.Security;

import com.example.lab2.model.security.User;
import com.example.lab2.model.security.UserBuilder;

import java.sql.*;
import java.util.Optional;

import static com.example.lab2.database.Constants.TABLES.USER;

public class UserRepositorySQL implements UserRepository {

    private final Connection connection;
    private final RoleRepository roleRepository;

    public UserRepositorySQL(Connection connection, RoleRepository roleRepository) {
        this.connection = connection;
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        try {
            Statement statement = connection.createStatement();

            String fetchUserSql =
                    "Select * from `" + USER + "` where `username`=\'" + username + "\' and `password`=\'" + password + "\'";
            ResultSet userResultSet = statement.executeQuery(fetchUserSql);

            if(userResultSet.next()) {
                UserBuilder userBuilder = new UserBuilder()
                        .setUsername(userResultSet.getString("username"))
                        .setPassword(userResultSet.getString("password"));
                roleRepository.findRolesForUser(userResultSet.getLong("id"))
                        .ifPresent(userBuilder::setRoles);
                return Optional.of(userBuilder.build());
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(Long id) {
        try {
            Statement statement = connection.createStatement();
            String fetchUserSQL = "Select * from " + USER + " where id = ?;";

            ResultSet resultSet = statement.executeQuery(fetchUserSQL);
            resultSet.next();

            if(resultSet.next()) {
                UserBuilder userBuilder = new UserBuilder()
                        .setUsername(resultSet.getString("username"))
                        .setPassword(resultSet.getString("password"));
                roleRepository.findRolesForUser(resultSet.getLong("id"))
                        .ifPresent(userBuilder::setRoles);
                return Optional.of(userBuilder.build());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public User create(User user) throws SQLException {
        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO user values (null, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertUserStatement.setString(1, user.getUsername());
            insertUserStatement.setString(2, user.getPassword());
            insertUserStatement.executeUpdate();

            ResultSet rs = insertUserStatement.getGeneratedKeys();
            rs.next();
            long userId = rs.getLong(1);
            user.setId(userId);

            roleRepository.addRolesToUser(user, user.getRoles());

            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
    }

    @Override
    public void deleteAll() {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from user where id >= 0";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
