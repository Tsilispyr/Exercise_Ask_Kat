package com.example.Ask.Service;

import com.example.Ask.Entities.*;
import com.example.Ask.Repositories.AnimalRepository;
import com.example.Ask.Repositories.RequestRepository;
import com.example.Ask.Repositories.RoleRepository;
import com.example.Ask.Repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Service
public class InitialService {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    UserRepository userRepository;
    UserService userService;
    RoleRepository roleRepository;
    AnimalRepository animalRepository;
    AnimalService animalService;
    RequestService requestService;
    RequestRepository requestRepository;
    public InitialService(UserRepository userRepository, UserService userService, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder, AnimalRepository animalRepository, AnimalService animalService, RequestRepository requestRepository, RequestService requestService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.animalRepository = animalRepository;
        this.animalService = animalService;
        this.requestRepository = requestRepository;
        this.requestService = requestService;
    }

    public void UserInitial() {
        this.userRepository.findByUsername("user").orElseGet(() -> {
            User user = new User("user", "user@hua.gr", this.passwordEncoder.encode("user"));
            Role role = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);
            userRepository.save(user);
            return null;
        });

        this.userRepository.findByUsername("admin").orElseGet(() -> {
            User admin = new User("admin", "admin@hua.gr", this.passwordEncoder.encode("admin"));
            Role role = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            admin.setRoles(roles);
            userRepository.save(admin);
            return null;
        });
        this.userRepository.findByUsername("shelter").orElseGet(() -> {
            User admin = new User("shelter", "shelter@hua.gr", this.passwordEncoder.encode("shelter"));
            Role role = roleRepository.findByName("ROLE_SHELTER")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            admin.setRoles(roles);
            userRepository.save(admin);
            return null;
        });
        this.userRepository.findByUsername("Doctor").orElseGet(() -> {
            User admin = new User("Doctor", "Doctor@hua.gr", this.passwordEncoder.encode("Doctor"));
            Role role = roleRepository.findByName("ROLE_DOCTOR")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            admin.setRoles(roles);
            userRepository.save(admin);
            return null;
        });

}
public void AnimalInitial() {
    createRequestIfNotFound("Alex", 8, Gender.Male, "Dog");
    createRequestIfNotFound("Coco", 8, Gender.Male, "Parrot");

    createAnimalIfNotFound("Pepper", 2, Gender.Female, "Cat");
    createAnimalIfNotFound("Nova", 1, Gender.Male, "Dog");
}

    private void createRequestIfNotFound(String name, int age, Gender gender, String type) {
        this.requestRepository.findByName(name).orElseGet(() -> {
            Request request = new Request();
            request.setAge(age);
            request.setGender(gender);
            request.setType(type);
            request.setName(name);
            requestService.saveRequest(request);
            return null;
        });
    }

    private void createAnimalIfNotFound(String name, int age, Gender gender, String type) {
        this.animalRepository.findByName(name).orElseGet(() -> {
            Animal animal = new Animal();
            animal.setAge(age);
            animal.setGender(gender);
            animal.setType(type);
            animal.setName(name);
            animalService.saveAnimal(animal);
            return null;
        });
    }
@PostConstruct
    public void init() {
        this.UserInitial();
        this.AnimalInitial();
    }
}

