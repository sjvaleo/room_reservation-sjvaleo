package edu.p566.roomreservation.entity;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "buildings")
public class Building {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Floor> floors = new ArrayList<>();

  // --- Get and Set ---
  public Long getId() { return id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public List<Floor> getFloors() { return floors; }
  public void setFloors(List<Floor> floors) { this.floors = floors; }
}
