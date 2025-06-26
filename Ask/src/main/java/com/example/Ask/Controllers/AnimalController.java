package com.example.Ask.Controllers;

import com.example.Ask.Entities.Animal;
import com.example.Ask.Service.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/animals")
public class AnimalController {
    @Autowired private AnimalService animalService;

    @GetMapping("/available")
    public List<Animal> getAvailableAnimals() {
        return animalService.getAvailableAnimals();
    }

    @PostMapping
    public Animal createAnimal(@RequestBody Animal animal) {
        return animalService.createAnimal(animal);
    }

    @PutMapping("/{id}")
    public Animal updateAnimal(@PathVariable Long id, @RequestBody Animal animal) {
        return animalService.updateAnimal(id, animal);
    }

    @DeleteMapping("/{id}")
    public void deleteAnimal(@PathVariable Long id) {
        animalService.deleteAnimal(id);
    }
}