package edu.p566.roomreservation.controller;

import edu.p566.roomreservation.repo.FloorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  private final FloorRepository floors;

  public HomeController(FloorRepository floors) {
    this.floors = floors;
  }

  @GetMapping("/")
  public String home() {
    return "search";
  }

  @GetMapping("/floors")
  public String listFloors(Model model) {
    model.addAttribute("floors", floors.findAll());
    return "floors";
  }
}
