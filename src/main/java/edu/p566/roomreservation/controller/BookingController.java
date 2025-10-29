package edu.p566.roomreservation.controller;

import edu.p566.roomreservation.entity.Reservation;
import edu.p566.roomreservation.repo.ReservationRepository;
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
@RequestMapping("/bookings")
public class BookingController {

    private final ReservationRepository reservationRepo;

    public BookingController(ReservationRepository reservationRepo) {
        this.reservationRepo = reservationRepo;
    }

    @GetMapping
    public String myBookings(Model model, Principal principal) {
        String who = principal.getName();
        List<Reservation> mine = reservationRepo.findAllByBookedByOrderByStartDateTimeDesc(who);
        model.addAttribute("bookings", mine);
        model.addAttribute("who", who);
        return "bookings";
    }

    @GetMapping("/confirm/{id}")
    public String confirm(@PathVariable Long id, Model model, Principal principal) {
        Reservation b = reservationRepo.findById(id).orElseThrow();
        if (principal == null || !b.getBookedBy().equals(principal.getName())) throw new IllegalArgumentException("Unauthorized");
        model.addAttribute("booking", b);
        return "booking_confirm";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, Principal principal) {
        Reservation b = reservationRepo.findById(id).orElseThrow();
        if (principal == null || !b.getBookedBy().equals(principal.getName())) throw new IllegalArgumentException("Unauthorized");
        model.addAttribute("booking", b);
        model.addAttribute("date", b.getStartDateTime().toLocalDate());
        model.addAttribute("time", b.getStartDateTime().toLocalTime());
        int duration = (int) java.time.Duration.between(b.getStartDateTime(), b.getEndDateTime()).toMinutes();
        model.addAttribute("durationMinutes", duration);
        return "booking_detail";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                         @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime time,
                         @RequestParam int durationMinutes,
                         Principal principal) {
        Reservation b = reservationRepo.findById(id).orElseThrow();
        if (principal == null || !b.getBookedBy().equals(principal.getName())) throw new IllegalArgumentException("Unauthorized");
        LocalDateTime start = LocalDateTime.of(date, time);
        LocalDateTime end = start.plusMinutes(durationMinutes);
        boolean conflict = reservationRepo.existsByRoomAndIdNotAndStartDateTimeLessThanAndEndDateTimeGreaterThan(
                b.getRoom(), b.getId(), end, start);
        if (conflict) throw new IllegalStateException("That time conflicts with another booking for this room.");
        b.setStartDateTime(start);
        b.setEndDateTime(end);
        reservationRepo.save(b);
        return "redirect:/bookings/{id}";
    }

    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id, Principal principal) {
        Reservation b = reservationRepo.findById(id).orElseThrow();
        if (principal == null || !b.getBookedBy().equals(principal.getName())) throw new IllegalArgumentException("Unauthorized");
        reservationRepo.delete(b);
        return "redirect:/bookings";
    }
}
