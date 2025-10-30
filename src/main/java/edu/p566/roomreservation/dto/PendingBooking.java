package edu.p566.roomreservation.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class PendingBooking implements Serializable {
    private Long roomId;
    private LocalDate date;
    private LocalTime time;
    private int durationMinutes;

    public PendingBooking() {}

    public PendingBooking(Long roomId, LocalDate date, LocalTime time, int durationMinutes) {
        this.roomId = roomId;
        this.date = date;
        this.time = time;
        this.durationMinutes = durationMinutes;
    }

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
}
