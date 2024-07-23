package dal;

import model.Event;
import model.EventRegistration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO extends DBContext {

    // Existing methods...
    public void createEvent(Event event) throws SQLException {
        String sql = "INSERT INTO Event (title, description, start_time, end_time, created_by) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, event.getTitle());
            stmt.setString(2, event.getDescription());
            stmt.setTimestamp(3, Timestamp.valueOf(event.getStartTime()));
            stmt.setTimestamp(4, Timestamp.valueOf(event.getEndTime()));
            stmt.setInt(5, event.getCreatedBy());
            stmt.executeUpdate();
        }
    }

    public void updateEvent(Event event) throws SQLException {
        String sql = "UPDATE Event SET title = ?, description = ?, start_time = ?, end_time = ? WHERE event_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, event.getTitle());
            stmt.setString(2, event.getDescription());
            stmt.setTimestamp(3, Timestamp.valueOf(event.getStartTime()));
            stmt.setTimestamp(4, Timestamp.valueOf(event.getEndTime()));
            stmt.setInt(5, event.getEventId());
            stmt.executeUpdate();
        }
    }

    public void deleteEvent(int eventId) throws SQLException {
        String sql = "DELETE FROM Event WHERE event_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            stmt.executeUpdate();
        }
    }

    public Event getEventById(int eventId) throws SQLException {
        String sql = "SELECT * FROM Event WHERE event_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Event(
                            rs.getInt("event_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getTimestamp("start_time").toLocalDateTime(),
                            rs.getTimestamp("end_time").toLocalDateTime(),
                            rs.getInt("created_by"),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getTimestamp("updated_at").toLocalDateTime()
                    );
                }
            }
        }
        return null;
    }

    public List<Event> getAllEvents() throws SQLException {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM Event";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                events.add(new Event(
                        rs.getInt("event_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getTimestamp("end_time").toLocalDateTime(),
                        rs.getInt("created_by"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime()
                ));
            }
        }
        return events;
    }

    public void registerForEvent(int eventId, int userId) throws SQLException {
        String sql = "INSERT INTO EventRegistration (event_id, user_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    public List<EventRegistration> getRegistrationsByEvent(int eventId) throws SQLException {
        List<EventRegistration> registrations = new ArrayList<>();
        String sql = "SELECT * FROM EventRegistration WHERE event_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    registrations.add(new EventRegistration(
                            rs.getInt("registration_id"),
                            rs.getInt("event_id"),
                            rs.getInt("user_id"),
                            rs.getString("status"),
                            rs.getTimestamp("registered_at").toLocalDateTime()
                    ));
                }
            }
        }
        return registrations;
    }

    public void updateRegistrationStatus(int registrationId, String status) throws SQLException {
        String sql = "UPDATE EventRegistration SET status = ? WHERE registration_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, registrationId);
            stmt.executeUpdate();
        }
    }

    public List<EventRegistration> getRegistrationsByUser(int userId) throws SQLException {
        List<EventRegistration> registrations = new ArrayList<>();
        String sql = "SELECT * FROM EventRegistration WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    registrations.add(new EventRegistration(
                            rs.getInt("registration_id"),
                            rs.getInt("event_id"),
                            rs.getInt("user_id"),
                            rs.getString("status"),
                            rs.getTimestamp("registered_at").toLocalDateTime()
                    ));
                }
            }
        }
        return registrations;
    }
}
