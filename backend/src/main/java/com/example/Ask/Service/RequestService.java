package com.example.Ask.Service;

import com.example.Ask.Entities.Animal;
import com.example.Ask.Repositories.AnimalRepository;
import com.example.Ask.Repositories.RequestRepository;
import jakarta.persistence.Column;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.example.Ask.Entities.Request;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class RequestService {
    private final AnimalService animalService;
    private RequestRepository requestRepository;
    private RequestService requestService;
    @Autowired
    private EmailService emailService;

    public RequestService(RequestRepository requestRepository, AnimalService animalService) {
        this.requestRepository = requestRepository;
        this.requestService = this;
        this.animalService = animalService;
    }

    @Transactional
    public List<Request> getRequests() {
        return requestRepository.findAll();
    }

    @Transactional
    public void saveRequest(Request request) {
        requestRepository.save(request);
        // Send confirmation email to user
        if (request.getUser() != null && request.getUser().getEmail() != null) {
            emailService.send(
                request.getUser().getEmail(),
                "Your adoption request has been submitted",
                "Your request for adopting animal '" + request.getName() + "' has been submitted and is pending approval."
            );
        }
    }

    @Transactional
    public Request getRequest(Integer id) {
        return requestRepository.findById(id).get();
    }
    @Transactional
    public void DelRequest(Request request) {
        requestRepository.delete(request);
    }

    @Transactional
    public void CheckRequest(Request request) {
        // If any approval is -1 (denied), send denied email and delete request
        if (request.getDocApproved() == -1 || request.getAdminApproved() == -1 || request.getShelterApproved() == -1) {
            if (request.getUser() != null && request.getUser().getEmail() != null) {
                emailService.send(
                    request.getUser().getEmail(),
                    "Adoption Request Decision",
                    "Your adoption request for animal '" + request.getName() + "' was denied."
                );
            }
            requestService.DelRequest(request);
            return;
        }
        // If all are approved (==1), send approved email and delete request
        if (request.getDocApproved() == 1 && request.getAdminApproved() == 1 && request.getShelterApproved() == 1) {
            if (request.getUser() != null && request.getUser().getEmail() != null) {
                emailService.send(
                    request.getUser().getEmail(),
                    "Adoption Request Decision",
                    "Your adoption request for animal '" + request.getName() + "' was approved!"
                );
            }
            Animal animal = new Animal();
            animal.setAge(request.getAge());
            animal.setGender(request.getGender());
            animal.setType(request.getType());
            animal.setName(request.getName());
            animalService.saveAnimal(animal);
            requestService.DelRequest(request);
        }
    }
}
