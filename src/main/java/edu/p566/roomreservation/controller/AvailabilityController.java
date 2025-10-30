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

    // Accept BOTH GET and POST for results to make links/forms flexible
    @RequestMapping(value = "/availability/results", method = {RequestMethod.GET, RequestMethod.POST})
    public String availabilityResults(Model model,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                      @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime time,
                                      @RequestParam(defaultValue = "60") Integer durationMinutes,
                                      @RequestParam(required = false) Long floorId,
                                      // optional filters coming from search.html
                                      @RequestParam(required = false) Integer minCapacity,
                                      @RequestParam(required = false, defaultValue = "false") boolean hasWhiteboard,
                                      @RequestParam(required = false) Integer minTv) {

        LocalDateTime start = LocalDateTime.of(date, time);
        LocalDateTime end = start.plusMinutes(durationMinutes != null ? durationMinutes : 60);

        // Base availability query
        List<Room> available = reservationService.findAvailableRooms(start, end, floorId);

        // Apply optional filters (null-safe)
        List<Room> filtered = available.stream()
                .filter(r -> (minCapacity == null) || (r.getCapacity() == null) || r.getCapacity() >= minCapacity)
                .filter(r -> !hasWhiteboard || Boolean.TRUE.equals(r.getWhiteboard()))
                .filter(r -> (minTv == null) || (r.getTvCount() == null) || r.getTvCount() >= minTv)
                .toList();

        model.addAttribute("availableRooms", filtered);
        model.addAttribute("date", date);
        model.addAttribute("time", time);
        model.addAttribute("durationMinutes", durationMinutes);
        model.addAttribute("floorId", floorId);

        model.addAttribute("minCapacity", minCapacity);
        model.addAttribute("hasWhiteboard", hasWhiteboard);
        model.addAttribute("minTv", minTv);

        return "availability_results"; // NOTE: plural file name
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

        var booking = reservationService.book(
                roomId,
                start,
                end,
                principal != null ? principal.getName() : "Guest"
        );

        return "redirect:/bookings/confirm/" + booking.getId();
    }
}
