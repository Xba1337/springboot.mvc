package ru.spring.mvc.util;

import org.springframework.stereotype.Component;
import ru.spring.mvc.dto.UserDto;
import ru.spring.mvc.model.User;

@Component
public class UserDtoConverter {

    private final PetDtoConverter petDtoConverter;

    public UserDtoConverter(PetDtoConverter petDtoConverter) {
        this.petDtoConverter = petDtoConverter;
    }

    public User convertToUser(UserDto userDto) {
        return new User(userDto.getId(),
                userDto.getName(),
                userDto.getEmail(),
                userDto.getAge(),
                userDto.getPets().stream().map(petDtoConverter::convertToPet).toList());
    }

    public UserDto convertToDto(User user) {
        return new UserDto(user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getPets().stream().map(petDtoConverter::convertToDto).toList());
    }
}
