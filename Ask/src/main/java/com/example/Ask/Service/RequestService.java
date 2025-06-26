package com.example.Ask.Service;

import com.example.Ask.Entities.Animal;
import com.example.Ask.Repositories.AnimalRepository;
import com.example.Ask.Repositories.RequestRepository;
import jakarta.persistence.Column;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.example.Ask.Entities.Request;
import java.util.List;
import com.example.Ask.Service.EmailService;

@Service
public class RequestService {
    private final AnimalService animalService;
    private RequestRepository requestRepository;
    private RequestService requestService;
    private final EmailService emailService;

    public RequestService(RequestRepository requestRepository, AnimalService animalService, EmailService emailService) {
        this.requestRepository = requestRepository;
        this.requestService = this;
        this.animalService = animalService;
        this.emailService = emailService;
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

    @Transactional
    public List<Request> getPendingRequests() {
        return requestRepository.findAll().stream()
            .filter(r -> r.getAdminApproved() == 0 || r.getDocApproved() == 0)
            .toList();
    }

    @Transactional
    public Request createRequest(Request req) {
        req.setAdminApproved(0);
        req.setDocApproved(0);
        Request saved = requestRepository.save(req);
        String email = (req.getUser() != null && req.getUser().getEmail() != null) ? req.getUser().getEmail() : "user@demo.local";
        emailService.send(email, "Request Submitted", "Your request for animal '" + req.getName() + "' has been submitted and is pending approval.");
        return saved;
    }

    @Transactional
    public void approveRequest(Long id) {
        Request req = requestRepository.findById(id.intValue()).orElseThrow();
        req.setAdminApproved(1);
        req.setDocApproved(1);
        requestRepository.save(req);
        if (req.getAdminApproved() == 1 && req.getDocApproved() == 1) {
            Animal animal = new Animal();
            animal.setAge(req.getAge());
            animal.setGender(req.getGender());
            animal.setType(req.getType());
            animal.setName(req.getName());
            animalService.saveAnimal(animal);
            requestRepository.delete(req);
            String email = (req.getUser() != null && req.getUser().getEmail() != null) ? req.getUser().getEmail() : "user@demo.local";
            emailService.send(email, "Request Approved", "Your request for animal '" + req.getName() + "' has been approved and the animal is now available for adoption.");
        }
    }

    @Transactional
    public void denyRequest(Long id) {
        Request req = requestRepository.findById(id.intValue()).orElseThrow();
        requestRepository.delete(req);
        String email = (req.getUser() != null && req.getUser().getEmail() != null) ? req.getUser().getEmail() : "user@demo.local";
        emailService.send(email, "Request Denied", "Your request for animal '" + req.getName() + "' has been denied.");
    }

    @Transactional
    public List<Request> getRequestsByUsername(String username) {
        return requestRepository.findAll().stream()
            .filter(r -> r.getUser() != null && username.equals(r.getUser().getUsername()))
            .toList();
    }
}

