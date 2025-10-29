package edu.p566.roomreservation.service;

import edu.p566.roomreservation.entity.Reservation;
import edu.p566.roomreservation.entity.Room;
import edu.p566.roomreservation.repo.ReservationRepository;
import edu.p566.roomreservation.repo.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepo;
    private final RoomRepository roomRepo;

    public ReservationService(ReservationRepository reservationRepo, RoomRepository roomRepo) {
        this.reservationRepo = reservationRepo;
        this.roomRepo = roomRepo;
    }

    @Transactional(readOnly = true)
    public List<Room> findAvailableRooms(LocalDateTime start, LocalDateTime end, Long floorId) {
        List<Room> rooms = roomRepo.findAll();
        if (floorId != null) {
            rooms = rooms.stream()
                    .filter(r -> r.getFloor() != null && r.getFloor().getId() != null && r.getFloor().getId().equals(floorId))
                    .collect(Collectors.toList());
        }
        return rooms.stream()
                .filter(r -> !reservationRepo.existsByRoomAndStartDateTimeLessThanAndEndDateTimeGreaterThan(r, end, start))
                .collect(Collectors.toList());
    }

    @Transactional
    public Reservation book(Long roomId, LocalDateTime start, LocalDateTime end, String bookedBy) {
        Room room = roomRepo.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Room not found"));
        boolean overlaps = reservationRepo.existsByRoomAndStartDateTimeLessThanAndEndDateTimeGreaterThan(room, end, start);
        if (overlaps) {
            throw new IllegalStateException("Room is no longer available for that time window.");
        }
        Reservation res = new Reservation();
        res.setRoom(room);
        res.setStartDateTime(start);
        res.setEndDateTime(end);
        res.setBookedBy((bookedBy == null || bookedBy.isBlank()) ? "Guest" : bookedBy.trim());
        return reservationRepo.save(res);
    }
}
