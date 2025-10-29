package edu.p566.roomreservation.dto;

public class RoomFilter {
    private String q;            // name contains
    private Integer floor;       // exact floor
    private Integer minCapacity; // >=
    private Boolean tv;          // must have tv
    private Boolean whiteboard;  // must have whiteboard
    private Boolean available;   // true/false

    public String getQ() { return q; }
    public void setQ(String q) { this.q = q; }
    public Integer getFloor() { return floor; }
    public void setFloor(Integer floor) { this.floor = floor; }
    public Integer getMinCapacity() { return minCapacity; }
    public void setMinCapacity(Integer minCapacity) { this.minCapacity = minCapacity; }
    public Boolean getTv() { return tv; }
    public void setTv(Boolean tv) { this.tv = tv; }
    public Boolean getWhiteboard() { return whiteboard; }
    public void setWhiteboard(Boolean whiteboard) { this.whiteboard = whiteboard; }
    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }
}
