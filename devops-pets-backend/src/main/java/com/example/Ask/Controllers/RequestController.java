package com.example.Ask.Controllers;

import com.example.Ask.Entities.Request;
import com.example.Ask.Repositories.RequestRepository;
import com.example.Ask.Service.AnimalService;
import com.example.Ask.Service.RequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.example.Ask.Entities.Animal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.Ask.Service.UserService;
import com.example.Ask.Entities.User;

@Controller
@RequestMapping("Request")
public class RequestController {

    private AnimalService animalService;
    private RequestService requestService;
    private UserService userService;
    public RequestController(RequestService requestService,AnimalService animalService, UserService userService) {
        this.requestService = requestService;
        this.animalService = animalService;
        this.userService = userService;
    }

    @RequestMapping("")
    public String showRequests(Model model,Model model1,Integer check) {
        model.addAttribute("Requests", requestService.getRequests());
        return "Animal/Request";
    }
    @GetMapping("/{id}")
    public String showRequest(@PathVariable Integer id, Model model){
        Request request = requestService.getRequest(id);
        model.addAttribute("request", request);
        return "Animal/Request";
    }
    @GetMapping("/Delete/{id}")
    public String deleteRequest(@PathVariable Integer id, Model model){
        Request request = requestService.getRequest(id);
        requestService.DelRequest(request);
        model.addAttribute("Requests", requestService.getRequests());
        return "Animal/Request";
    }
    @GetMapping("/Approve/{id}")
    public String AdminApprove(@PathVariable Integer id, Model model,Model model1,Integer check) {
        Request request = requestService.getRequest(id);
        request.setAdminApproved(1);
        requestService.CheckRequest(request);
        model.addAttribute("Requests", requestService.getRequests());
        return "Animal/Request";
    }
    @GetMapping("/ApproveD/{id}")
    public String DocApprove(@PathVariable Integer id, Model model,Model model1,Integer check){
        Request request = requestService.getRequest(id);
        request.setDocApproved(1);
        requestService.CheckRequest(request);
        model.addAttribute("Requests", requestService.getRequests());
        return "Animal/Request";
    }
    @GetMapping("/ApproveS/{id}")
    public String ShelterApprove(@PathVariable Integer id, Model model, Model model1, Integer check) {
        Request request = requestService.getRequest(id);
        request.setShelterApproved(1);
        requestService.CheckRequest(request);
        model.addAttribute("Requests", requestService.getRequests());
        return "Animal/Request";
    }
    @GetMapping("/DenyS/{id}")
    public String ShelterDeny(@PathVariable Integer id, Model model, Model model1, Integer check) {
        Request request = requestService.getRequest(id);
        request.setShelterApproved(-1);
        requestService.CheckRequest(request);
        model.addAttribute("Requests", requestService.getRequests());
        return "Animal/Request";
    }
    @GetMapping("/Deny/{id}")
    public String AdminDeny(@PathVariable Integer id, Model model, Model model1, Integer check) {
        Request request = requestService.getRequest(id);
        request.setAdminApproved(-1);
        requestService.CheckRequest(request);
        model.addAttribute("Requests", requestService.getRequests());
        return "Animal/Request";
    }
    @GetMapping("/DenyD/{id}")
    public String DocDeny(@PathVariable Integer id, Model model, Model model1, Integer check) {
        Request request = requestService.getRequest(id);
        request.setDocApproved(-1);
        requestService.CheckRequest(request);
        model.addAttribute("Requests", requestService.getRequests());
        return "Animal/Request";
    }
    @GetMapping("/new")
    public String addRequest(Model model){
        Request request = new Request();
        model.addAttribute("request", request);
        return "Animal/Animal";

    }

    @PostMapping("/new")
    public String saveRequest(@ModelAttribute("request") Request request, BindingResult theBindingResult, Model model) {
        if (theBindingResult.hasErrors()) {
            System.out.println("error");
            return "Animal/Animal";
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            request.setUser(user);
            requestService.saveRequest(request);
            model.addAttribute("Requests", requestService.getRequests());
            return "Animal/Request";
        }

    }
}
