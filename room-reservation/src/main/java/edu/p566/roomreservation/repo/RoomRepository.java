package edu.p566.roomreservation.repo;

import edu.p566.roomreservation.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
  List<Room> findByFloorIdOrderByNameAsc(Long floorId);
}




