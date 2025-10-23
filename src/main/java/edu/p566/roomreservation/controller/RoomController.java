package edu.p566.roomreservation.controller;

import edu.p566.roomreservation.entity.Room;
import edu.p566.roomreservation.repo.RoomRepository;
import edu.p566.roomreservation.service.AvailabilityService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/rooms")
public class RoomController {
  private final RoomRepository rooms;
  private final AvailabilityService availability;

  public RoomController(RoomRepository rooms, AvailabilityService availability) {
    this.rooms = rooms;
    this.availability = availability;
  }

  @GetMapping("/by-floor/{floorId}")
  public String byFloor(@PathVariable Long floorId, Model model) {
    model.addAttribute("rooms", rooms.findByFloorIdOrderByNameAsc(floorId));
    model.addAttribute("floorId", floorId);
    return "rooms"; // templates/rooms.html
  }

  @GetMapping("/{id}")
  public String details(@PathVariable Long id,
                        @RequestParam(required = false)
                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                        Model model) {
    Room room = rooms.findById(id).orElseThrow();
    LocalDate d = (date == null) ? LocalDate.now() : date;

    model.addAttribute("room", room);
    model.addAttribute("date", d);
    model.addAttribute("slots", availability.dailySlots(id, d));
    return "room-details"; // templates/room-details.html
  }
}
