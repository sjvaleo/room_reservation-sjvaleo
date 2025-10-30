package edu.p566.roomreservation.repo;

import edu.p566.roomreservation.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepository extends JpaRepository<Building, Long> {
}
