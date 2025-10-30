package edu.p566.roomreservation.repo;

import edu.p566.roomreservation.entity.Floor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FloorRepository extends JpaRepository<Floor, Long> {
}
