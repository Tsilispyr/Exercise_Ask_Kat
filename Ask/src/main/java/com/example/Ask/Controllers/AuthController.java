package com.example.Ask.Controllers;

import com.example.Ask.Entities.Role;
import com.example.Ask.Entities.User;
import com.example.Ask.Repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.Ask.Service.UserService;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import java.util.Set;
import java.util.HashSet;
import java.util.Optional;

@RestController
public class AuthController {

    private final BCryptPasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    UserService userService;
    public AuthController(RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void setup() {
      Role role_user = new Role("ROLE_USER");
      Role role_admin = new Role("ROLE_ADMIN");
      Role role_doctor = new Role("ROLE_DOCTOR");
      Role role_shelter = new Role("ROLE_SHELTER");

      roleRepository.updateOrInsert(role_user);
      roleRepository.updateOrInsert(role_admin);
      roleRepository.updateOrInsert(role_doctor);
      roleRepository.updateOrInsert(role_shelter);
    }

    @GetMapping("/login")
    public Map<String, String> login() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Login endpoint (API)");
        return response;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String email = payload.get("email");
        String password = payload.get("password");
        String roleName = payload.get("role");
        if (username == null || email == null || password == null || roleName == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Missing fields"));
        }
        Optional<Role> roleOpt = roleRepository.findByName(roleName);
        if (roleOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid role"));
        }
        User user = new User(username, email, password, "pending");
        Set<Role> roles = new HashSet<>();
        roles.add(roleOpt.get());
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(password));
        userService.saveUserWithRoles(user, roles);
        return ResponseEntity.ok(Map.of("message", "Registration successful. Awaiting admin approval."));
    }
}
