package edu.p566.roomreservation.entity;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "rooms")
public class Room {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private Integer capacity;
  private Integer tvCount;
  private Boolean whiteboard;

  @ManyToOne(fetch = FetchType.LAZY)
  private Floor floor;

  @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Reservation> reservations = new ArrayList<>();

  // --- Get and Set ---
  public Long getId() { return id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public Integer getCapacity() { return capacity; }
  public void setCapacity(Integer capacity) { this.capacity = capacity; }

  public Integer getTvCount() { return tvCount; }
  public void setTvCount(Integer tvCount) { this.tvCount = tvCount; }

  public Boolean getWhiteboard() { return whiteboard; }
  public void setWhiteboard(Boolean whiteboard) { this.whiteboard = whiteboard; }

  public Floor getFloor() { return floor; }
  public void setFloor(Floor floor) { this.floor = floor; }

  public List<Reservation> getReservations() { return reservations; }
  public void setReservations(List<Reservation> reservations) { this.reservations = reservations; }
}
