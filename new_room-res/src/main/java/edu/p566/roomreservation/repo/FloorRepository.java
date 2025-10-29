package edu.p566.roomreservation.repo;

import edu.p566.roomreservation.entity.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FloorRepository extends JpaRepository<Floor, Long> {
  List<Floor> findByBuildingIdOrderByNumberAsc(Long buildingId);
}
