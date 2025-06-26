package com.example.Ask.Service;

import com.example.Ask.Repositories.AnimalRepository;
import jakarta.persistence.Column;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.example.Ask.Entities.Animal;
import java.util.List;
import com.example.Ask.Service.EmailService;


@Service
public class AnimalService {
    private AnimalRepository AnimalRepo;
    private AnimalService animalservice;
    private EmailService emailService;
    public AnimalService(AnimalRepository AnimalRepo, EmailService emailService) {
        this.AnimalRepo = AnimalRepo;
        this.animalservice = this;
        this.emailService = emailService;
    }

    @Transactional
    public List<Animal> getAnimals() {
        return AnimalRepo.findAll();
    }

    @Transactional
    public Animal saveAnimal(Animal animal) {
        animal.setReq(0);
        AnimalRepo.save(animal);
        return animal;
    }

    @Transactional
    public Animal getAnimal(Integer id) {
        return AnimalRepo.findById(id).orElse(null);
    }
    @Transactional
    public void Delanimal(Animal animal) {
        AnimalRepo.delete(animal);
    }

    @Transactional
    public void delAnimal(Integer id) {
        AnimalRepo.deleteById(id);
    }

    @Transactional
    public Animal createAnimal(Animal animal) {
        animal.setReq(0);
        Animal saved = AnimalRepo.save(animal);
        emailService.send("admin@petsystem.local", "New Animal Added", "A new animal '" + animal.getName() + "' was added.");
        return saved;
    }

    @Transactional
    public Animal updateAnimal(Long id, Animal animal) {
        animal.setId(id.intValue());
        Animal updated = AnimalRepo.save(animal);
        emailService.send("admin@petsystem.local", "Animal Updated", "Animal '" + animal.getName() + "' was updated.");
        return updated;
    }

    @Transactional
    public void deleteAnimal(Long id) {
        Animal animal = AnimalRepo.findById(id.intValue()).orElse(null);
        if (animal != null) {
            emailService.send("admin@petsystem.local", "Animal Deleted", "Animal '" + animal.getName() + "' was deleted.");
        }
        AnimalRepo.deleteById(id.intValue());
    }

    @Transactional
    public List<Animal> getAvailableAnimals() {
        return AnimalRepo.findAll();
    }

}




