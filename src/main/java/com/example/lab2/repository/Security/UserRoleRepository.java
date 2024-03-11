package com.example.lab2.repository.Security;

import com.example.lab2.model.security.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository {
    void create(long userId, long roleId);
    Optional<List<UserRole>> findByUserId(long userId);
}
