package edu.p566.roomreservation.config;

import edu.p566.roomreservation.entity.*;
import edu.p566.roomreservation.repo.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {


  @Bean
  CommandLineRunner seed(BuildingRepository bRepo,
                         FloorRepository fRepo,
                         RoomRepository rRepo,
                         ReservationRepository resRepo) { // keep for future use; not used now
    return args -> {
      // Skip if already seeded
      if (bRepo.count() > 0) return;

      // ----- Building -----
      Building b = new Building();
      b.setName("Luddy'");
      bRepo.save(b);

    
      int[] roomsPerFloor = {12, 13, 11, 10};

      for (int fNum = 1; fNum <= 4; fNum++) {
        // ----- Floor -----
        Floor f = new Floor();
        f.setName("Floor " + fNum);
        f.setNumber(fNum);
        f.setBuilding(b);
        fRepo.save(f);

        int totalRooms = roomsPerFloor[fNum - 1];

        // rooms either have 1 / 2 whiteboards
        int noWhiteboardCount = (fNum % 2 == 0) ? 2 : 1; 

        for (int i = 1; i <= totalRooms; i++) {
          Room r = new Room();

          // Name: F{floor}-{roomIndex}, padded to 2 digits
          String roomName = "F" + fNum + "-" + String.format("%02d", i);
          r.setName(roomName);

          // Capacity: random spread between 6 and 80
          // Distributes across the range for variety within each floor
          int capMin = 6, capMax = 80;
          int capRange = capMax - capMin;
          int capacity = capMin + ((i - 1) * Math.max(1, capRange / Math.max(1, totalRooms - 1)));
          if (capacity > capMax) capacity = capMax;
          r.setCapacity(capacity);

          // TVs: between 1–4
          int tvCount = 1 + ((i - 1) % 4);
          r.setTvCount(tvCount);

          // Whiteboard: only 1–2 rooms per floor without whiteboard
          boolean hasWhiteboard = i > noWhiteboardCount;
          r.setWhiteboard(hasWhiteboard);

          // Relationship
          r.setFloor(f);

          rRepo.save(r);
        }
      }

    
    };
  }
}
