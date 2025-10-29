package edu.p566.roomreservation.controller;

import edu.p566.roomreservation.entity.Room;
import edu.p566.roomreservation.service.ReservationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
  import java.time.LocalTime;
import java.util.List;

@Controller
public class SearchController {

  private final ReservationService reservationService;

  public SearchController(ReservationService reservationService) {
    this.reservationService = reservationService;
  }

  @GetMapping("/search")
  public String showSearchForm(Model model) {
    model.addAttribute("date", LocalDate.now());
    return "search";
  }

  @PostMapping("/search")
  public String doSearch(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                         @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime start,
                         @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime end,
                         @RequestParam(required = false, defaultValue = "1") Integer minCapacity,
                         @RequestParam(required = false, defaultValue = "false") boolean needsTv,
                         @RequestParam(required = false, defaultValue = "false") boolean needsWhiteboard,
                         Model model) {

    LocalTime realEnd = end.isAfter(start) ? end : start.plusMinutes(30);
    LocalDateTime startDt = LocalDateTime.of(date, start);
    LocalDateTime endDt = LocalDateTime.of(date, realEnd);
    int durationMinutes = (int) Duration.between(startDt, endDt).toMinutes();

    List<Room> availableForWindow = reservationService.findAvailableRooms(startDt, endDt, null);

    List<Room> filtered = availableForWindow.stream()
        .filter(r -> (minCapacity == null || r.getCapacity() == null || r.getCapacity() >= minCapacity))
        .filter(r -> !needsTv || (r.getTvCount() != null && r.getTvCount() > 0))
        .filter(r -> !needsWhiteboard || Boolean.TRUE.equals(r.getWhiteboard()))
        .toList();

    model.addAttribute("date", date);
    model.addAttribute("start", start);
    model.addAttribute("end", realEnd);
    model.addAttribute("durationMinutes", durationMinutes);
    model.addAttribute("minCapacity", minCapacity);
    model.addAttribute("needsTv", needsTv);
    model.addAttribute("needsWhiteboard", needsWhiteboard);
    model.addAttribute("rooms", filtered);

    return "search";
  }
}
