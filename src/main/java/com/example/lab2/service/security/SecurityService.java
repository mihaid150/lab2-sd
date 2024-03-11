package com.example.lab2.service.security;

import com.example.lab2.model.security.ERole;
import com.example.lab2.model.security.Role;
import com.example.lab2.model.security.User;
import com.example.lab2.model.security.UserBuilder;
import com.example.lab2.repository.Security.RoleRepository;
import com.example.lab2.repository.Security.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class SecurityService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public SecurityService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User register(String username, String password) throws SQLException {
        String encodedPassword = encodePassword(password);

        Optional<Role> optionalCustomerRole = roleRepository.findRoleByTitle(ERole.CUSTOMER);
        if (optionalCustomerRole.isEmpty()) {
            throw new IllegalStateException("Customer role not found");
        }

        Role customerRole = optionalCustomerRole.get();

        User user = new UserBuilder()
                .setUsername(username)
                .setPassword(encodedPassword)
                .setRoles(List.of(customerRole))
                .build();
        roleRepository.addRolesToUser(user, List.of(customerRole));
        return userRepository.create(user);
    }

    public User login(String username, String password) {
        Optional<User> optionalUser= userRepository.findByUsernameAndPassword(username, encodePassword(password));
        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("No user found with these credentials");
        }
        return optionalUser.get();
    }

    private String encodePassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
