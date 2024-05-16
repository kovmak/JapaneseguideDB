package com.krnelx.persistence.entity;

import java.util.UUID;

public record Users(UUID id,
                   String login,
                   String password,
                   UsersRole role, // Перейменовано Role на UsersRole
                   String name) implements Entity, Comparable<Users>  {

    @Override
    public int compareTo(Users Users) {
        return this.login.compareTo(Users.login);
    }

    public String getPassword() { return this.password;
    }

    public enum UsersRole { // Перейменовано Role на UsersRole
        ADMIN("admin"),
        MODER("moder"),
        CLIENT("client");

        String roleName;

        UsersRole(String roleName) {
            this.roleName = roleName;
        }

        public String getName(){
            return roleName;
        }
    }
}
