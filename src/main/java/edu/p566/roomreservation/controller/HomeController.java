package edu.p566.roomreservation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Make the site publicly land on Search (not Login)
    @GetMapping("/")
    public String root() {
        return "search";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
