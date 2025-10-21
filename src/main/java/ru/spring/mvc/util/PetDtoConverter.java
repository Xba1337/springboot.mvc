package ru.spring.mvc.util;

import org.springframework.stereotype.Component;
import ru.spring.mvc.dto.PetDto;
import ru.spring.mvc.model.Pet;

@Component
public class PetDtoConverter {

    public Pet convertToPet(PetDto petDto) {
        return new Pet(petDto.getId(),
                petDto.getName(),
                petDto.getUserId());
    }

    public PetDto convertToDto(Pet pet) {
        return new PetDto(pet.getId(),
                pet.getName(),
                pet.getUserId());
    }
}
