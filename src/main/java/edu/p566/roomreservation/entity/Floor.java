package edu.p566.roomreservation.entity;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "floors")
public class Floor {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private Integer number;

  @ManyToOne(fetch = FetchType.LAZY)
  private Building building;

  @OneToMany(mappedBy = "floor", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Room> rooms = new ArrayList<>();

  // --- Get and Set ---
  public Long getId() { return id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public Integer getNumber() { return number; }
  public void setNumber(Integer number) { this.number = number; }

  public Building getBuilding() { return building; }
  public void setBuilding(Building building) { this.building = building; }

  public List<Room> getRooms() { return rooms; }
  public void setRooms(List<Room> rooms) { this.rooms = rooms; }
}
