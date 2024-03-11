package com.example.lab2.repository.Security;

import com.example.lab2.model.security.ERole;
import com.example.lab2.model.security.Role;
import com.example.lab2.model.security.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.lab2.database.Constants.TABLES.ROLE;
import static com.example.lab2.database.Constants.TABLES.USER_ROLE;

public class RoleRepositorySQL implements RoleRepository{

    private final Connection connection;

    public RoleRepositorySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(ERole role) {
        try {
            PreparedStatement inserStatement = connection.prepareStatement("INSERT IGNORE INTO " + ROLE + " values (null, ?)");
            inserStatement.setString(1, role.toString());
            inserStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Role> findRoleByTitle(ERole role) {
        Statement statement;
        try {
            statement = connection.createStatement();
            String fetchRoleSql = "Select * from " + ROLE + " where `role`=\'" + role.toString() + "\'";
            ResultSet roleResultSet = statement.executeQuery(fetchRoleSql);
            roleResultSet.next();
            Long roleId = roleResultSet.getLong("id");
            String roleTitle = roleResultSet.getString("role");
            return Optional.of(new Role(roleId, roleTitle));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<Role>> findRolesForUser(long userId) {
        List<Role> roles = new ArrayList<>();
        try {
            String query = "SELECT r.id, r.role FROM " + ROLE + " r INNER JOIN " + USER_ROLE +
                    " ur ON r.id=ur.role_id WHERE ur.user_id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, userId);

            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                long roleId = resultSet.getLong("id");
                String roleName = resultSet.getString("role");
                ERole eRole = ERole.valueOf(roleName);
                roles.add(new Role(roleId, roleName));
            }

            if (!roles.isEmpty()) {
                return Optional.of(roles);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public void addRolesToUser(User user, List<Role> roles) {
        String insertUserRoleSql = "INSERT IGNORE INTO " + USER_ROLE + " (user_id, role_id) VALUES (?, ?);";

        try (PreparedStatement statement = connection.prepareStatement(insertUserRoleSql)) {
            for (Role role : roles) {
                statement.setLong(1, user.getId());
                statement.setLong(2, role.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding roles to user", e);
        }
    }
}
