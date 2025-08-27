package ru.spring.mvc.service;

import org.springframework.stereotype.Service;
import ru.spring.mvc.model.User;

import java.util.*;

@Service
public class UserService {

    private final Map<Long, User> users;
    private final PetService petService;
    private long USER_ID_COUNTER;

    public UserService(PetService petService) {
        this.petService = petService;
        this.users = new HashMap<>();
        this.USER_ID_COUNTER = 0L;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public User getUserById(long id) {
        if (!users.containsKey(id)) {
            throw new NoSuchElementException("User with id " + id + " not found");
        }
        return users.get(id);
    }

    public User createUser(User user) {
        if (users.containsKey(user.getId())) {
            throw new IllegalArgumentException("User with id " + user.getId() + " already exists");
        }
        USER_ID_COUNTER++;

        User newUser = new User(
                USER_ID_COUNTER,
                user.getName(),
                user.getEmail(),
                user.getAge(),
                new ArrayList<>()
        );
        users.put(USER_ID_COUNTER, newUser);

        return newUser;
    }

    public User updateUser(long id, User user) {
        if (!users.containsKey(id)) {
            throw new NoSuchElementException("User with id " + id + " does not exist");
        }

        User updatedUser = new User(
                id,
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getPets()
        );
        users.put(id, updatedUser);

        return updatedUser;
    }

    public void deleteUser(long id) {
        if (!users.containsKey(id)) {
            throw new NoSuchElementException("User with id " + id + " does not exist");
        }
        petService.deletePetsByUserId(id);

        users.remove(id);
    }
}
