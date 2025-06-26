package com.example.Ask.Controllers;

import com.example.Ask.Entities.Request;
import com.example.Ask.Repositories.RequestRepository;
import com.example.Ask.Service.AnimalService;
import com.example.Ask.Service.RequestService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import com.example.Ask.Entities.Animal;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    private AnimalService animalService;
    @Autowired private RequestService requestService;
    public RequestController(RequestService requestService,AnimalService animalService) {
        this.requestService = requestService;
        this.animalService = animalService;
    }

    @GetMapping("/pending")
    public List<Request> getPendingRequests() {
        return requestService.getPendingRequests();
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public Request createRequest(@RequestBody Request req) {
        return requestService.createRequest(req);
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<?> approveRequest(@PathVariable Long id) {
        requestService.approveRequest(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/deny")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<?> denyRequest(@PathVariable Long id) {
        requestService.denyRequest(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping("")
    public List<Request> showRequests() {
        return requestService.getRequests();
    }
    @GetMapping("/{id}")
    public Request showRequest(@PathVariable Integer id){
        return requestService.getRequest(id);
    }

    @GetMapping("/Approve/{id}")
    public Request AdminApprove(@PathVariable Integer id) {
        Request request = requestService.getRequest(id);
        request.setAdminApproved(1);
        requestService.CheckRequest(request);
        return request;
    }
    @GetMapping("/ApproveD/{id}")
    public Request DocApprove(@PathVariable Integer id){
        Request request = requestService.getRequest(id);
        request.setDocApproved(1);
        return request;
    }
    @GetMapping("/new")
    public Request addRequest(){
        return new Request();
    }
    @PostMapping("/new")
    public List<Request> saveRequest(@RequestBody Request request) {
        requestService.saveRequest(request);
        return requestService.getRequests();
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Request updateRequest(@PathVariable Integer id, @RequestBody Request request) {
        request.setId(id);
        return requestService.saveRequest(request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteRequest(@PathVariable Integer id) {
        Request request = requestService.getRequest(id);
        requestService.DelRequest(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mine")
    @PreAuthorize("isAuthenticated()")
    public List<Request> getMyRequests(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaimAsString("preferred_username");
        return requestService.getRequestsByUsername(username);
    }
}
