package com.example.Ask.Service;

import com.example.Ask.Entities.Animal;
import com.example.Ask.Repositories.AnimalRepository;
import com.example.Ask.Repositories.RequestRepository;
import jakarta.persistence.Column;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.example.Ask.Entities.Request;
import java.util.List;

@Service
public class RequestService {
    private final AnimalService animalService;
    private RequestRepository requestRepository;
    private RequestService requestService;

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
    public Request saveRequest(Request request) {
        requestRepository.save(request);
        return request;
    }

    @Transactional
    public Request getRequest(Integer id) {
        return requestRepository.findById(id).orElse(null);
    }
    
    @Transactional
    public void DelRequest(Request request) {
        requestRepository.delete(request);
    }

    @Transactional
    public void CheckRequest(Request request) {
        int check = 1;
        if (request.getDocApproved() == 1 && request.getAdminApproved() == 1) {
            Animal animal = new Animal();
            animal.setAge(request.getAge());
            animal.setGender(request.getGender());
            animal.setType(request.getType());
            animal.setName(request.getName());
            animalService.saveAnimal(animal);
            requestService.DelRequest(request);
        }
    }

    // Όταν δημιουργείται νέο request
    public void submitRequest(Request request) {
        // save request...
        requestRepository.save(request);

        // Στείλε email στον χρήστη - commented out since Request entity doesn't have user/animal references
        // emailService.send(
        //     request.getUser().getEmail(), 
        //     "Your adoption request has been submitted",
        //     "Your request for adopting animal '" + request.getAnimal().getName() +
        //     "' has been submitted and is pending doctor approval."
        // );
    }

    // Όταν ο doctor κάνει approve/decline
    public void doctorDecision(Request request, boolean approved) {
        request.setDocApproved(approved ? 1 : 0);
        requestRepository.save(request);

        String decision = approved ? "approved" : "declined";

        // emailService.send(
        //     request.getUser().getEmail(),
        //     "Doctor Decision on Your Adoption Request",
        //     "The doctor has " + decision + " your request for animal '" + request.getAnimal().getName() + "'."
        // );
    }

    // Όταν ο admin κάνει approve/decline
    public void adminDecision(Request request, boolean approved) {
        request.setAdminApproved(approved ? 1 : 0);
        requestRepository.save(request);

        String decision = approved ? "approved" : "declined";

        // emailService.send(
        //     request.getUser().getEmail(),
        //     "Final Decision on Your Adoption Request",
        //     "The admin has " + decision + " your request for animal '" + request.getAnimal().getName() +
        //     "'. This is the final decision."
        // );
    }
}

