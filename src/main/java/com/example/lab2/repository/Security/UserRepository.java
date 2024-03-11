package com.example.lab2.repository.Security;

import com.example.lab2.model.security.User;

import java.sql.SQLException;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsernameAndPassword(String username, String password);
    Optional<User> findById(Long id);

    User create(User user) throws SQLException;

    void deleteAll();

//  Response<Boolean> existsByUsername(String email);
}
