package com.krnelx.domain.dto;

import com.krnelx.persistence.entity.Users.UsersRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record UserGudesDto(
    @NotNull(message = "User ID is missing")
    UUID id,
    @NotBlank(message = "User login cannot be empty")
    @Size(min = 6, max = 32, message = "User login must be between 6 and 32 characters long")
    String login,
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters long")
    String password,
    UsersRole role,
    @NotBlank(message = "User name cannot be empty")
    @Size(min = 6, max = 64, message = "User name must be between 6 and 64 characters long")
    String name

) {

}