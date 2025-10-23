package edu.p566.roomreservation.controller;

import edu.p566.roomreservation.repo.FloorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;

@Controller
public class HomeController {
  private final FloorRepository floors;

  public HomeController(FloorRepository floors) { this.floors = floors; }


  @GetMapping("/floors")
  public String floors(Model model) {
    model.addAttribute("floors", floors.findAll().stream()
        .sorted(Comparator.comparingInt(f -> f.getNumber() == null ? 0 : f.getNumber()))
        .toList());
    return "floors";
  }
}
