package ru.spring.mvc.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.spring.mvc.dto.UserDto;
import ru.spring.mvc.model.User;
import ru.spring.mvc.service.UserService;
import ru.spring.mvc.util.UserDtoConverter;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserDtoConverter userDtoConverter;

    public UserController(UserService userService, UserDtoConverter userDtoConverter) {
        this.userService = userService;
        this.userDtoConverter = userDtoConverter;
    }

    @GetMapping
    public List<UserDto> getUsers() {
        return userService
                .getAllUsers()
                .stream()
                .map(userDtoConverter::convertToDto)
                .toList();
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable long id) {
        return userDtoConverter
                .convertToDto(userService.getUserById(id));
    }

    @PostMapping()
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto) {
        User newUser = userService.createUser(userDtoConverter.convertToUser(userDto));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userDtoConverter.convertToDto(newUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable long id, @RequestBody @Valid UserDto userDto) {
        User updatedUser = userService.updateUser(id, userDtoConverter.convertToUser(userDto));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDtoConverter.convertToDto(updatedUser));
    }
}
