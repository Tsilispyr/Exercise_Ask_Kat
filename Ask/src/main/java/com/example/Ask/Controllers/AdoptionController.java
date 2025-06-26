package com.example.Ask.Controllers;

import com.example.Ask.Entities.AdoptionRequest;
import com.example.Ask.Service.AdoptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequestMapping("/api/adoptions")
public class AdoptionController {
    @Autowired private AdoptionService adoptionService;

    @GetMapping("/pending")
    public List<AdoptionRequest> getPendingAdoptions() {
        return adoptionService.getPendingAdoptions();
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AdoptionRequest> createAdoption(@RequestBody AdoptionRequest req) {
        AdoptionRequest saved = adoptionService.createAdoption(req);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/mine")
    @PreAuthorize("isAuthenticated()")
    public List<AdoptionRequest> getMyAdoptions(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaimAsString("preferred_username");
        return adoptionService.getAdoptionsByUsername(username);
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<?> approveAdoption(@PathVariable Long id) {
        adoptionService.approveAdoption(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/deny")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<?> denyAdoption(@PathVariable Long id) {
        adoptionService.denyAdoption(id);
        return ResponseEntity.ok().build();
    }
} 