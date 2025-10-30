package edu.p566.roomreservation.controller;

import edu.p566.roomreservation.dto.PendingBooking;
import edu.p566.roomreservation.service.ReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class PostLoginController {

    private final ReservationService reservationService;

    public PostLoginController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/post-login")
    public String afterLogin(HttpSession session, Principal principal) {
        if (principal == null) {
            return "redirect:/search";
        }
        Object o = session.getAttribute("PENDING_BOOK");
        if (o instanceof PendingBooking pending) {
            session.removeAttribute("PENDING_BOOK");
            var start = LocalDateTime.of(pending.getDate(), pending.getTime());
            var end   = start.plusMinutes(pending.getDurationMinutes());
            var booked = reservationService.book(pending.getRoomId(), start, end, principal.getName());
            return "redirect:/bookings/confirm/" + booked.getId();
        }
        // No pending action → land on My Bookings by default
        return "redirect:/bookings";
    }
}
