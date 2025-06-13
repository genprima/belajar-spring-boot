package com.nostratech.belajarspringboot.spring.dto;

import com.nostratech.belajarspringboot.spring.validator.annotation.ValidName;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthorDto(
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 25, message = "Name must be between 3 and 25 characters")
    @ValidName()
    String name, 

    String description) {

}
