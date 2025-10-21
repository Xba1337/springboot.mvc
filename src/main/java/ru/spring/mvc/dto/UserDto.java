package ru.spring.mvc.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Range;

import java.util.List;


public class UserDto {

    private Long id;

    @NotBlank
    @Size(max = 50)
    private String name;

    @Email
    private String email;

    @NotNull
    @Range(min = 1, max = 100)
    private Integer age;

    @NotNull
    private List<PetDto> petsDto;

    public UserDto() {
    }

    public UserDto(Long id, String name, String email, Integer age, List<PetDto> petDto) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.petsDto = petDto;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<PetDto> getPets() {
        return petsDto;
    }

    public void setPets(@NotNull List<PetDto> petsDto) {
        this.petsDto = petsDto;
    }

    public void addPet(PetDto petDto) {
        this.petsDto.add(petDto);
    }

    public void removePet(PetDto petDto) {
        this.petsDto.remove(petDto);
    }
}
