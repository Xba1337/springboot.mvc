package ru.spring.mvc.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PetDto {

    private Long id;

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotNull
    private Long userId;

    public PetDto() {
    }

    public PetDto(Long id, String name, Long userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
