package ru.spring.mvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.spring.mvc.dto.UserDto;
import ru.spring.mvc.model.User;
import ru.spring.mvc.service.UserService;
import ru.spring.mvc.util.UserDtoConverter;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserDtoConverter userDtoConverter;

    @Test
    void successCreateUser() throws Exception {
        UserDto userDto = new UserDto(
                null,
                "test",
                "test@mail.ru",
                99,
                List.of());

        String userJson = objectMapper.writeValueAsString(userDto);

        String createdUserJson = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto userResponse = objectMapper.readValue(createdUserJson, UserDto.class);

        assertEquals(userDto.getName(), userResponse.getName());
        assertEquals(userDto.getAge(), userResponse.getAge());
        assertEquals(userDto.getEmail(), userResponse.getEmail());
        assertEquals(userDto.getPets(), userResponse.getPets());
    }

    @Test
    void failureCreateUser() throws Exception {
        UserDto userDto = new UserDto(
                null,
                "test",
                "test",
                99,
                List.of());

        String userJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().is(500));

    }

    @Test
    void successDeleteUser() throws Exception {
        UserDto userDto = new UserDto(
                null,
                "test",
                "test@mail.ru",
                99,
                List.of());

        User user = userService.createUser(userDtoConverter.convertToUser(userDto));

        mockMvc.perform(delete("/users/{id}", user.getId()))
                .andExpect(status().is(200));

        assertThrows(NoSuchElementException.class, () -> userService.getUserById(user.getId()));
    }

    @Test
    void failureDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/{id}", Integer.MAX_VALUE))
                .andExpect(status().is(404));
    }

    @Test
    void updateUser() throws Exception {
        UserDto userDto = new UserDto(
                null,
                "test",
                "test@mail.ru",
                99,
                List.of());

        UserDto userDtoUpdated = new UserDto(
                null,
                "updated",
                "updated@mail.ru",
                11,
                List.of());

        User user = userService.createUser(userDtoConverter.convertToUser(userDto));
        User updatedUser = userService.createUser(userDtoConverter.convertToUser(userDtoUpdated));

        String userJson = objectMapper.writeValueAsString(updatedUser);

        String updateUserJson = mockMvc.perform(put("/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto updatedUserResponse = objectMapper.readValue(updateUserJson, UserDto.class);

        userService.deleteUser(user.getId());
        userService.deleteUser(updatedUser.getId());

        assertEquals("updated", updatedUserResponse.getName());
        assertEquals(11, updatedUserResponse.getAge());
        assertEquals("updated@mail.ru", updatedUserResponse.getEmail());
    }

    @Test
    void failureUpdateUserBecauseOfNotFound() throws Exception {
        UserDto userDto = new UserDto(
                null,
                "test",
                "test@mail.ru",
                99,
                List.of());

        UserDto userDtoUpdated = new UserDto(
                null,
                "updated",
                "updated@mail.ru",
                11,
                List.of());

        User user = userService.createUser(userDtoConverter.convertToUser(userDto));
        User updatedUser = userService.createUser(userDtoConverter.convertToUser(userDtoUpdated));

        String userJson = objectMapper.writeValueAsString(updatedUser);

        mockMvc.perform(put("/users/{id}", Integer.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().is(404));

        userService.deleteUser(user.getId());
        userService.deleteUser(updatedUser.getId());
    }

    @Test
    void failureUpdateUserBecauseOfInvalidData() throws Exception {
        UserDto userDto = new UserDto(
                null,
                "test",
                "test@mail.ru",
                99,
                List.of());

        UserDto userDtoUpdated = new UserDto(
                null,
                "updated",
                "updated",
                11,
                List.of());

        User user = userService.createUser(userDtoConverter.convertToUser(userDto));
        User updatedUser = userService.createUser(userDtoConverter.convertToUser(userDtoUpdated));

        String userJson = objectMapper.writeValueAsString(updatedUser);

        mockMvc.perform(put("/users/{id}", Integer.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().is(500));

        userService.deleteUser(user.getId());
        userService.deleteUser(updatedUser.getId());
    }

    @Test
    void successGetUserById() throws Exception {
        UserDto userDto = new UserDto(null,
                "test",
                "test@mail.ru",
                99,
                List.of());

        User user = userService.createUser(userDtoConverter.convertToUser(userDto));

        String userJson = mockMvc.perform(get("/users/{id}", user.getId()))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto userResponse = objectMapper.readValue(userJson, UserDto.class);

        assertEquals(userDto.getName(), userResponse.getName());
        assertEquals(userDto.getAge(), userResponse.getAge());
        assertEquals(userDto.getEmail(), userResponse.getEmail());
        assertEquals(userDto.getPets(), userResponse.getPets());
        userService.deleteUser(user.getId());
    }

    @Test
    void failGetUserById() throws Exception {
        mockMvc.perform(get("/users/{id}", Long.MAX_VALUE))
                .andExpect(status().is(404));
    }
}