package edu.p566.roomreservation.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute("loggedIn")
    public boolean loggedIn(Principal principal) {
        return principal != null;
    }

    @ModelAttribute("username")
    public String username(Principal principal) {
        return principal != null ? principal.getName() : null;
    }
}
