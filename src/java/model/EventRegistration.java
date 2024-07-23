package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventRegistration {
    private int registrationId;
    private int eventId;
    private int userId;
    private String status;
    private LocalDateTime registeredAt;

    // Constructor, getters, and setters

    public EventRegistration(int registrationId, int eventId, int userId, String status, LocalDateTime registeredAt) {
        this.registrationId = registrationId;
        this.eventId = eventId;
        this.userId = userId;
        this.status = status;
        this.registeredAt = registeredAt;
    }

    public int getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }
     public String getFormattedRegisteredAt() {
        return registeredAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
