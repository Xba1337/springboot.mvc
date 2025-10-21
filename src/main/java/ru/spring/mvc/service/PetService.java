package ru.spring.mvc.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.spring.mvc.model.Pet;
import ru.spring.mvc.model.User;

import java.util.*;

@Service
public class PetService {

    private final Map<Long, Pet> pets;
    private long PET_ID_COUNTER;
    private final UserService userService;

    public PetService(@Lazy UserService userService) {
        this.userService = userService;
        this.pets = new HashMap<>();
        this.PET_ID_COUNTER = 0L;
    }

    public List<Pet> getAllPets() {
        return new ArrayList<>(pets.values());
    }

    public Pet getPetById(long id) {
        if (!pets.containsKey(id)) {
            throw new NoSuchElementException("Pet with id " + id + " not found");
        }
        return pets.get(id);
    }

    public Pet createPet(Pet pet) {
        if (userService.getUserById(pet.getUserId()) == null) {
            throw new NoSuchElementException("Pet with id " + pet.getUserId() + " cannot be created, because user with id "
                    + pet.getUserId() + " does not exist");
        }

        if (pets.containsKey(pet.getId())) {
            throw new IllegalArgumentException("Pet with id " + pet.getId() + " already exists");
        }
        PET_ID_COUNTER++;

        Pet newPet = new Pet(
                PET_ID_COUNTER,
                pet.getName(),
                pet.getUserId()
        );

        pets.put(PET_ID_COUNTER, newPet);
        User userById = userService.getUserById(pet.getUserId());
        userById.addPet(newPet);

        return newPet;
    }

    public Pet updatePet(long id, Pet pet) {
        if (!pets.containsKey(id)) {
            throw new NoSuchElementException("Pet with id " + id + " does not exist");
        }
        Pet updatedPet = new Pet(
                id,
                pet.getName(),
                pet.getUserId()
        );
        Pet oldPet = pets.get(id);
        pets.put(id, updatedPet);

        User oldUser = userService.getUserById(oldPet.getUserId());
        User newUser = userService.getUserById(updatedPet.getUserId());
        oldUser.getPets().remove(oldPet);
        newUser.getPets().add(updatedPet);

        return updatedPet;
    }

    public void deletePet(long id) {
        if (!pets.containsKey(id)) {
            throw new NoSuchElementException("Pet with id " + id + " not found");
        }

        Pet petToRemove = pets.get(id);
        pets.remove(id);
        User userById = userService.getUserById(petToRemove.getUserId());
        userById.getPets().remove(petToRemove);
    }

    public void deletePetsByUserId(long userId) {
        List<Long> petsRemoveContainer = new ArrayList<>();
        for (Pet pet : pets.values()) {
            if (pet.getUserId() == userId) {
                petsRemoveContainer.add(pet.getId());
            }
        }
        for (Long petId : petsRemoveContainer) {
            deletePet(petId);
        }
    }
}
