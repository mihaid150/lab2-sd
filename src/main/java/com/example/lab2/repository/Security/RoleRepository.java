package com.example.lab2.repository.Security;

import com.example.lab2.model.security.ERole;
import com.example.lab2.model.security.Role;
import com.example.lab2.model.security.User;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {
    void create(ERole role);
    Optional<Role> findRoleByTitle(ERole role);
    Optional<List<Role>> findRolesForUser(long userId);

    void addRolesToUser(User user, List<Role> roles);
}
