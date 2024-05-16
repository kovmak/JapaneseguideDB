package com.krnelx.domain.dto;

import com.krnelx.persistence.entity.Users.UsersRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record UserUpdateDto(
    @NotNull(message = "Відсутній іденитфікатор користувача")
    UUID id,

    @Size(min = 6, max = 64, message = "Ім'я користувача має містити від 6 до 64 символів")
    String login,

    @Size(min = 8, max = 72, message = "Пароль повинен містити від 8 до 72 символів")
    String password,

    UsersRole role,

    @NotBlank(message = "Ім'я користувача не може бути порожнім")
    @Size(min = 6, max = 64, message = "Ім'я користувача має містити від 6 до 64 символів")
    String name
) {

}