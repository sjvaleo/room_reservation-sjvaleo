package edu.p566.roomreservation.controller;

import edu.p566.roomreservation.entity.Room;
import edu.p566.roomreservation.repo.RoomRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/rooms")
public class RoomController {
  private final RoomRepository rooms;

  public RoomController(RoomRepository rooms) {
    this.rooms = rooms;
  }

  @GetMapping("/by-floor/{floorId}")
  public String byFloor(@PathVariable Long floorId, Model model) {
    model.addAttribute("rooms", rooms.findByFloorIdOrderByNameAsc(floorId));
    model.addAttribute("floorId", floorId);
    return "rooms";
  }

  @GetMapping("/{id}")
  public String details(@PathVariable Long id,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                        @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime start,
                        @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime end,
                        @RequestParam(required = false) Integer minCapacity,
                        @RequestParam(required = false, defaultValue = "false") boolean needsTv,
                        @RequestParam(required = false, defaultValue = "false") boolean needsWhiteboard,
                        Model model) {
    Room room = rooms.findById(id).orElseThrow();

    Integer durationMinutes = null;
    if (start != null && end != null) {
      var startToday = start.atDate(date != null ? date : LocalDate.now());
      var endToday = end.atDate(date != null ? date : LocalDate.now());
      if (!end.isAfter(start)) {
        endToday = startToday.plusMinutes(30);
      }
      durationMinutes = (int) Duration.between(startToday, endToday).toMinutes();
      if (durationMinutes <= 0) durationMinutes = 30;
      model.addAttribute("end", end);
    }

    model.addAttribute("room", room);
    model.addAttribute("date", date);
    model.addAttribute("start", start);
    model.addAttribute("durationMinutes", durationMinutes);

    model.addAttribute("minCapacity", minCapacity);
    model.addAttribute("needsTv", needsTv);
    model.addAttribute("needsWhiteboard", needsWhiteboard);

    return "room-details";
  }
}
