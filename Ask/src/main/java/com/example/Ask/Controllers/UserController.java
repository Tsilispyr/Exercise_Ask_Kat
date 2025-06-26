package com.example.Ask.Controllers;

import com.example.Ask.Entities.User;
import com.example.Ask.Entities.Role;
import com.example.Ask.Repositories.RoleRepository;
import com.example.Ask.Repositories.UserRepository;
import com.example.Ask.Service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired private UserService userService;
    private RoleRepository roleRepository;

    public UserController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/register")
    public User register() {
        return new User();
    }

    @PostMapping("/saveUser")
    public Map<String, Object> saveUser(@RequestBody User user){
        Integer id = userService.saveUser(user);
        Map<String, Object> response = new HashMap<>();
        response.put("msg", "User '"+id+"' saved successfully !");
        return response;
    }

    @GetMapping("/users")
    public List<User> showUsers(){
        return userService.getUsers();
    }

    @GetMapping("/user/{user_id}")
    public User showUser(@PathVariable Long user_id){
        return userService.getUser(user_id);
    }

    @PostMapping("/user/{user_id}")
    public User saveStudent(@PathVariable Long user_id, @RequestBody User user) {
        user.setId(user_id.intValue());
        userService.saveUser(user);
        return user;
    }

    @GetMapping("/user/role/delete/{user_id}/{role_id}")
    public User deleteRolefromUser(@PathVariable Long user_id, @PathVariable Integer role_id){
        User user = (User) userService.getUser(user_id);
        Role role = roleRepository.findById(role_id).get();
        user.getRoles().remove(role);
        userService.updateUser(user);
        return user;
    }

    @GetMapping("/user/role/add/{user_id}/{role_id}")
    public User addRoletoUser(@PathVariable Long user_id, @PathVariable Integer role_id){
        User user = (User) userService.getUser(user_id);
        Role role = roleRepository.findById(role_id).get();
        user.getRoles().add(role);
        userService.updateUser(user);
        return user;
    }

    @PostMapping("/register")
    public Map<String, Object> registerUser(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String email = payload.get("email");
        String password = payload.get("password");
        String roleName = payload.get("role");
        User user = new User(username, email, password, "pending");
        Role role = roleRepository.findByName(roleName).orElseThrow(() -> new RuntimeException("Role not found"));
        user.getRoles().add(role);
        userService.saveUserWithRoles(user, user.getRoles());
        Map<String, Object> response = new HashMap<>();
        response.put("msg", "Registration successful! Awaiting admin approval.");
        return response;
    }
}