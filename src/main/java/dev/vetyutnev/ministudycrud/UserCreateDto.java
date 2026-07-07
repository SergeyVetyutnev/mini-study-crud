package dev.vetyutnev.ministudycrud;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UserCreateDto(
        @NotBlank(message = "пустое имя")
        String name,
        @Min(value = 0, message = "возраст не может быть меньше нуля")
        int age
) {
}
