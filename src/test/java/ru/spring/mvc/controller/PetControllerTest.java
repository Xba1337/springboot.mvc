package ru.spring.mvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.spring.mvc.dto.PetDto;
import ru.spring.mvc.model.Pet;
import ru.spring.mvc.model.User;
import ru.spring.mvc.service.PetService;
import ru.spring.mvc.service.UserService;
import ru.spring.mvc.util.PetDtoConverter;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetService petService;

    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private UserService userService;
    @Autowired
    private PetDtoConverter petDtoConverter;

    @Test
    void successCreatePet() throws Exception {
        User user = userService.createUser(new User(
                null,
                "test",
                "test@mail.ru",
                99,
                List.of()
        ));

        PetDto petDto = new PetDto(
                null,
                "test",
                user.getId()
        );

        String petJson = objectMapper.writeValueAsString(petDto);

        String createPetJson = mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petJson))
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        userService.deleteUser(user.getId());

        PetDto createPetDto = objectMapper.readValue(createPetJson, PetDto.class);

        Assertions.assertEquals(petDto.getName(), createPetDto.getName());
        Assertions.assertEquals(petDto.getUserId(), createPetDto.getUserId());
    }

    @Test
    void failureCreatePet() throws Exception {
        PetDto petDto = new PetDto(
                null,
                "test",
                null
        );

        String petJson = objectMapper.writeValueAsString(petDto);

        mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petJson))
                .andExpect(status().is(500));
    }

    @Test
    void successDeletePet() throws Exception {
        User user = userService.createUser(new User(
                null,
                "test",
                "test@mail.ru",
                99,
                List.of()
        ));

        PetDto petDto = new PetDto(
                null,
                "test",
                user.getId()
        );

        Pet pet = petService.createPet(petDtoConverter.convertToPet(petDto));

        mockMvc.perform(delete("/pets/{id}", pet.getId()))
                .andExpect(status().is(200));

        assertThrows(NoSuchElementException.class, () -> petService.getPetById(pet.getId()));
        userService.deleteUser(user.getId());
    }

    @Test
    void failureDeletePet() throws Exception {
        mockMvc.perform(delete("/pets/{id}", Integer.MAX_VALUE))
                .andExpect(status().is(404));
    }

    @Test
    void updatePet() throws Exception {
        User user = userService.createUser(new User(
                null,
                "test",
                "test@mail.ru",
                99,
                List.of()
        ));

        PetDto petDto = new PetDto(
                null,
                "test",
                user.getId()
        );

        PetDto petDtoUpdated = new PetDto(
                null,
                "updated",
                user.getId()
        );

        Pet pet = petService.createPet(petDtoConverter.convertToPet(petDto));
        Pet updatedPet = petService.createPet(petDtoConverter.convertToPet(petDtoUpdated));

        String petJson = objectMapper.writeValueAsString(updatedPet);

        String updatedPetJson = mockMvc.perform(put("/pets/{id}", pet.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petJson))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        userService.deleteUser(user.getId());

        PetDto updatedPetDto = objectMapper.readValue(updatedPetJson, PetDto.class);

        Assertions.assertEquals("updated", updatedPetDto.getName());
    }

    @Test
    void failureUpdatePetBecauseOfNotFound() throws Exception {
        User user = userService.createUser(new User(
                null,
                "test",
                "test@mail.ru",
                99,
                List.of()
        ));

        PetDto petDto = new PetDto(
                null,
                "test",
                user.getId()
        );

        PetDto petDtoUpdated = new PetDto(
                null,
                "updated",
                user.getId()
        );

        petService.createPet(petDtoConverter.convertToPet(petDto));
        Pet updatedPet = petService.createPet(petDtoConverter.convertToPet(petDtoUpdated));

        String petJson = objectMapper.writeValueAsString(updatedPet);

        mockMvc.perform(put("/pets/{id}", Integer.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petJson))
                .andExpect(status().is(404));

        userService.deleteUser(user.getId());
    }

    @Test
    void failureUpdatePetBecauseOfInvalidData() throws Exception {
        User user = userService.createUser(new User(
                null,
                "test",
                "test@mail.ru",
                99,
                List.of()
        ));

        PetDto petDto = new PetDto(
                null,
                "test",
                user.getId()
        );

        PetDto petDtoUpdated = new PetDto(
                null,
                null,
                user.getId()
        );

        petService.createPet(petDtoConverter.convertToPet(petDto));
        Pet updatedPet = petService.createPet(petDtoConverter.convertToPet(petDtoUpdated));

        String petJson = objectMapper.writeValueAsString(updatedPet);

        mockMvc.perform(put("/pets/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petJson))
                .andExpect(status().is(500));

        userService.deleteUser(user.getId());
    }

    @Test
    void successGetPetById() throws Exception{
        User user = userService.createUser(new User(
                null,
                "test",
                "test@mail.ru",
                99,
                List.of()
        ));

        PetDto petDto = new PetDto(
                null,
                "test",
                user.getId()
        );

        Pet pet = petService.createPet(petDtoConverter.convertToPet(petDto));

        String createdPetJson = mockMvc.perform(get("/pets/{id}", pet.getId()))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        PetDto createdPetDto = objectMapper.readValue(createdPetJson, PetDto.class);

        Assertions.assertEquals(petDto.getName(), createdPetDto.getName());
        Assertions.assertEquals(pet.getUserId(), createdPetDto.getUserId());
        userService.deleteUser(user.getId());
    }

    @Test
    void failureGetPetById() throws Exception {
        mockMvc.perform(get("/pets/{id}", Integer.MAX_VALUE))
                .andExpect(status().is(404));
    }
}
