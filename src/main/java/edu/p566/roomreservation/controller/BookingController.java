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

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final ReservationRepository reservationRepo;

    public BookingController(ReservationRepository reservationRepo) {
        this.reservationRepo = reservationRepo;
    }

    @GetMapping
    public String myBookings(Model model, Principal principal) {
        String who = (principal != null ? principal.getName() : "Guest");
        model.addAttribute("who", who);
        if (principal != null) {
            model.addAttribute("bookings", reservationRepo.findAllByBookedByOrderByStartDateTimeDesc(who));
        }
        return "bookings";
    }

    @GetMapping("/{id}")
    public String bookingDetail(@PathVariable Long id, Model model, Principal principal) {
        Reservation b = reservationRepo.findById(id).orElseThrow();
        requireOwnerOrGuest(b, principal);

        // Pre-fill the edit form values so users don't retype them
        LocalDate date = b.getStartDateTime().toLocalDate();
        LocalTime time = b.getStartDateTime().toLocalTime();
        int durationMinutes = (int) java.time.Duration.between(
                b.getStartDateTime(), b.getEndDateTime()
        ).toMinutes();

        model.addAttribute("booking", b);
        model.addAttribute("date", date);
        model.addAttribute("time", time);
        model.addAttribute("durationMinutes", durationMinutes);

        return "booking_detail";
    }

    @GetMapping("/confirm/{id}")
    public String bookingConfirm(@PathVariable Long id, Model model, Principal principal) {
        Reservation b = reservationRepo.findById(id).orElseThrow();
        requireOwnerOrGuest(b, principal);
        model.addAttribute("booking", b);
        return "booking_confirm";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                         @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime time,
                         @RequestParam int durationMinutes,
                         Principal principal) {

        Reservation b = reservationRepo.findById(id).orElseThrow();
        requireOwnerOrGuest(b, principal);

        LocalDateTime start = LocalDateTime.of(date, time);
        LocalDateTime end = start.plusMinutes(durationMinutes);

        boolean conflict = reservationRepo
                .existsByRoomAndIdNotAndStartDateTimeLessThanAndEndDateTimeGreaterThan(
                        b.getRoom(), b.getId(), end, start);

        if (conflict) {
            throw new IllegalStateException("That time conflicts with another booking for this room.");
        }

        b.setStartDateTime(start);
        b.setEndDateTime(end);
        reservationRepo.save(b);
        return "redirect:/bookings/{id}";
    }

    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id, Principal principal) {
        Reservation b = reservationRepo.findById(id).orElseThrow();
        requireOwnerOrGuest(b, principal);
        reservationRepo.delete(b);
        return "redirect:/bookings";
    }

    private void requireOwnerOrGuest(Reservation b, Principal principal) {
        String owner = b.getBookedBy();
        String user = (principal != null ? principal.getName() : null);

        if (owner == null) return;
        if ("Guest".equalsIgnoreCase(owner)) return;
        if (user != null && owner.equals(user)) return;

        throw new IllegalArgumentException("Unauthorized");
    }
}
