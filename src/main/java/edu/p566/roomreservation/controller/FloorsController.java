package edu.p566.roomreservation.controller;

import edu.p566.roomreservation.repo.FloorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FloorsController {

    private final FloorRepository floorRepo;

    public FloorsController(FloorRepository floorRepo) {
        this.floorRepo = floorRepo;
    }

    @GetMapping("/floors")
    public String floors(Model model) {
        model.addAttribute("floors", floorRepo.findAll());
        return "floors";
    }
}
