package ru.spring.mvc.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.spring.mvc.dto.PetDto;
import ru.spring.mvc.model.Pet;
import ru.spring.mvc.service.PetService;
import ru.spring.mvc.util.PetDtoConverter;

import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;
    private final PetDtoConverter petDtoConverter;

    public PetController(PetService petService, PetDtoConverter petDtoConverter) {
        this.petService = petService;
        this.petDtoConverter = petDtoConverter;
    }

    @GetMapping
    public List<PetDto> getAllPets() {
        return petService
                .getAllPets()
                .stream()
                .map(petDtoConverter::convertToDto)
                .toList();
    }

    @GetMapping("/{id}")
    public PetDto getPetById(@PathVariable long id) {
        return petDtoConverter.convertToDto(petService.getPetById(id));
    }

    @PostMapping
    public ResponseEntity<PetDto> createPet(@RequestBody @Valid PetDto petDto) {
        Pet newPet = petService.createPet(petDtoConverter.convertToPet(petDto));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(petDtoConverter.convertToDto(newPet));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable long id) {
        petService.deletePet(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetDto> updatePet(@PathVariable long id, @RequestBody @Valid PetDto pet) {
        Pet updatedPet = petService.updatePet(id, petDtoConverter.convertToPet(pet));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(petDtoConverter.convertToDto(updatedPet));
    }
}
