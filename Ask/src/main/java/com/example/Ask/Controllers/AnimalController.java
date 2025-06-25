package com.example.Ask.Controllers;

import com.example.Ask.Service.AnimalService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.example.Ask.Entities.Animal;
import com.example.Ask.Entities.Gender;
import java.util.List;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("Animal")
public class AnimalController {

    private AnimalService animalservice;

    public AnimalController(AnimalService animalservice) {
        this.animalservice = animalservice;
    }

    @RequestMapping("")
    public List<Animal> showAnimals() {
        return animalservice.getAnimals();
    }

    @GetMapping("/{id}")
    public Animal showAnimal(@PathVariable Integer id){
        return animalservice.getAnimal(id);
    }

    @PostMapping("")
    public Animal createAnimal(@RequestBody Animal animal) {
        return animalservice.saveAnimal(animal);
    }

    @PutMapping("/{id}")
    public Animal updateAnimal(@PathVariable Integer id, @RequestBody Animal animal) {
        animal.setId(id);
        return animalservice.saveAnimal(animal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable Integer id) {
        Animal animal = animalservice.getAnimal(id);
        animalservice.Delanimal(animal);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/Request/{id}")
    public Animal requestAnimal(@PathVariable Integer id) {
        Animal animal = animalservice.getAnimal(id);
        animal.setReq(1);
        return animalservice.saveAnimal(animal);
    }

    @PutMapping("/Deny/{id}")
    public Animal denyAnimal(@PathVariable Integer id) {
        Animal animal = animalservice.getAnimal(id);
        animal.setReq(0);
        return animalservice.saveAnimal(animal);
    }

}