package edu.p566.roomreservation.controller;

import edu.p566.roomreservation.entity.Room;
import edu.p566.roomreservation.repo.FloorRepository;
import edu.p566.roomreservation.service.ReservationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping
public class AvailabilityController {

    private final ReservationService reservationService;
    private final FloorRepository floorRepo;

    public AvailabilityController(ReservationService reservationService, FloorRepository floorRepo) {
        this.reservationService = reservationService;
        this.floorRepo = floorRepo;
    }

    @GetMapping("/availability")
    public String availabilityForm(Model model) {
        model.addAttribute("floors", floorRepo.findAll());
        return "availability";
    }

    @PostMapping("/availability/results")
    public String availabilityResults(Model model,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                      @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime time,
                                      @RequestParam(defaultValue = "60") int durationMinutes,
                                      @RequestParam(required = false) Long floorId) {

        LocalDateTime start = LocalDateTime.of(date, time);
        LocalDateTime end = start.plusMinutes(durationMinutes);

        List<Room> available = reservationService.findAvailableRooms(start, end, floorId);
        model.addAttribute("availableRooms", available);
        model.addAttribute("date", date);
        model.addAttribute("time", time);
        model.addAttribute("durationMinutes", durationMinutes);
        model.addAttribute("floorId", floorId);

        return "availability_results";
    }

    @PostMapping("/book")
    public String bookRoom(Model model,
                           @RequestParam Long roomId,
                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                           @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime time,
                           @RequestParam int durationMinutes,
                           Principal principal) {

        LocalDateTime start = LocalDateTime.of(date, time);
        LocalDateTime end = start.plusMinutes(durationMinutes);

        var booking = reservationService.book(roomId, start, end,
                principal != null ? principal.getName() : "Guest");

        return "redirect:/bookings/confirm/" + booking.getId();
    }
}
