package edu.p566.roomreservation.repo;

import edu.p566.roomreservation.entity.Reservation;
import edu.p566.roomreservation.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByRoomAndStartDateTimeLessThanAndEndDateTimeGreaterThan(
            Room room, LocalDateTime endExclusive, LocalDateTime startInclusive
    );

    List<Reservation> findAllByRoomAndStartDateTimeLessThanAndEndDateTimeGreaterThan(
            Room room, LocalDateTime endExclusive, LocalDateTime startInclusive
    );

    List<Reservation> findAllByBookedByOrderByStartDateTimeDesc(String bookedBy);

    boolean existsByRoomAndIdNotAndStartDateTimeLessThanAndEndDateTimeGreaterThan(
            Room room, Long id, LocalDateTime endExclusive, LocalDateTime startInclusive
    );
}
