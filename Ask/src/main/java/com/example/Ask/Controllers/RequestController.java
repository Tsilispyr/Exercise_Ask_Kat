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

@Controller
@RequestMapping("Request")
public class RequestController {

    private AnimalService animalService;
    private RequestService requestService;
    public RequestController(RequestService requestService,AnimalService animalService) {
        this.requestService = requestService;
        this.animalService = animalService;
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
            requestService.saveRequest(request);
            model.addAttribute("Requests", requestService.getRequests());
            return "Animal/Request";
        }

    }
}
