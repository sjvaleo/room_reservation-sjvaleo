package edu.p566.roomreservation.service;

import edu.p566.roomreservation.entity.Reservation;
import edu.p566.roomreservation.repo.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AvailabilityService {

  public record Slot(LocalDateTime start, LocalDateTime end, String status) {}

  private final ReservationRepository reservations;

  public AvailabilityService(ReservationRepository reservations) {
    this.reservations = reservations;
  }

  public boolean isAvailable(Long roomId, LocalDate date, LocalTime start, LocalTime end) {
  var startDT = date.atTime(start);
  var endDT = date.atTime(end);
  // Any overlapping reservation blocks availability?
  return reservations.findByRoomIdAndStartTimeBetween(
      roomId, startDT.minusHours(6), endDT.plusHours(6)
  ).stream().noneMatch(r ->
      r.getStartTime().isBefore(endDT) && r.getEndTime().isAfter(startDT)
  );
}


  /** Returns 30-minute slots from 08:00–18:00 with status "available" or "booked". */
  public List<Slot> dailySlots(Long roomId, LocalDate date) {
    LocalDateTime dayStart = date.atTime(8, 0);
    LocalDateTime dayEnd   = date.atTime(18, 0);

    // Pad the search window to catch edge overlaps
    List<Reservation> dayRes = reservations.findByRoomIdAndStartTimeBetween(
        roomId, dayStart.minusHours(6), dayEnd.plusHours(6));

    List<Slot> out = new ArrayList<>();
    for (LocalDateTime t = dayStart; t.isBefore(dayEnd); t = t.plusMinutes(30)) {
      LocalDateTime next = t.plusMinutes(30);

      // Make final copies for lambda capture
      final LocalDateTime slotStart = t;
      final LocalDateTime slotEnd   = next;

      // Overlap rule: [resStart, resEnd) overlaps [slotStart, slotEnd) if
      // resStart < slotEnd AND resEnd > slotStart
      boolean booked = dayRes.stream().anyMatch(r ->
          r.getStartTime().isBefore(slotEnd) && r.getEndTime().isAfter(slotStart)
      );

      out.add(new Slot(slotStart, slotEnd, booked ? "booked" : "available"));
    }
    return out;
  }
}
