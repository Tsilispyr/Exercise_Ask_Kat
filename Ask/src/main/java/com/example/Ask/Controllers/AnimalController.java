package com.example.Ask.Controllers;

import com.example.Ask.Service.AnimalService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.example.Ask.Entities.Animal;
import com.example.Ask.Entities.Gender; 


@Controller
@RequestMapping("Animal")
public class AnimalController {

    private AnimalService animalservice;

    public AnimalController(AnimalService animalservice) {
        this.animalservice = animalservice;
    }

    @RequestMapping("")
    public String showAnimals(Model model,Model model1,Integer check) {
        model.addAttribute("Animals", animalservice.getAnimals());
        return "Animal/Animals";
    }

    @GetMapping("/{id}")
    public String showAnimal(@PathVariable Integer id, Model model){
        Animal Animal = animalservice.getAnimal(id);
        model.addAttribute("Animals", Animal);
        return "Animal/Animals";
    }

    @GetMapping("/Delete/{id}")
    public String deleteAnimal(@PathVariable Integer id, Model model){
        Animal Animal = animalservice.getAnimal(id);
        animalservice.Delanimal(Animal);
        model.addAttribute("Animals", animalservice.getAnimals());
        return "Animal/Animals";
    }

    @GetMapping("/Request/{id}")
    public String RequestAnimal(@PathVariable Integer id, Model model){
        Animal Animal = animalservice.getAnimal(id);
        Animal.setReq(1);
        model.addAttribute("Animals", animalservice.getAnimals());
        return "Animal/Animals";
    }
    @GetMapping("/Deny/{id}")
    public String DenyAnimal(@PathVariable Integer id, Model model){
        Animal Animal = animalservice.getAnimal(id);
        Animal.setReq(0);
        model.addAttribute("Animals", animalservice.getAnimals());
        return "Animal/Animals";
    }



}