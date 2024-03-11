package com.example.lab2.model.security;

public class UserRole {
    private Long id;
    private Long userId;
    private Long roleId;
    public UserRole(Long id, Long userId, Long roleId) {
        this.id = id;
        this.roleId = roleId;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
