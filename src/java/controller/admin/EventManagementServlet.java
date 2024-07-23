package controller.admin;

import dal.EventDAO;
import model.Event;
import model.EventRegistration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;

@WebServlet(name = "EventManagementServlet", urlPatterns = {"/admin/event-manager"})
public class EventManagementServlet extends HttpServlet {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        EventDAO eventDAO = new EventDAO();
        if (a != null && a.getRoleAccount().getRole_id() == 1) {
            String action = request.getParameter("action");
            try {
                if ("viewRegistrations".equals(action)) {
                    int eventId = Integer.parseInt(request.getParameter("event_id"));
                    List<EventRegistration> registrations = eventDAO.getRegistrationsByEvent(eventId);
                    request.setAttribute("registrations", registrations);
                    request.setAttribute("event", eventDAO.getEventById(eventId));
                    request.getRequestDispatcher("event-registrations.jsp").forward(request, response);
                } else {
                    List<Event> events = eventDAO.getAllEvents();
                    request.setAttribute("events", events);
                    request.getRequestDispatcher("event-manager.jsp").forward(request, response);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                response.sendRedirect("../login");
            }
        } else {
            response.sendRedirect("../login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        EventDAO eventDAO = new EventDAO();
        if (a != null && a.getRoleAccount().getRole_id() == 1) {
            String action = request.getParameter("action");
            try {
                if (null != action) {
                    switch (action) {
                        case "create" -> {
                            String title = request.getParameter("title");
                            String description = request.getParameter("description");
                            LocalDateTime startTime = LocalDateTime.parse(request.getParameter("start_time"), formatter);
                            LocalDateTime endTime = LocalDateTime.parse(request.getParameter("end_time"), formatter);
                            Event event = new Event(title, description, startTime, endTime, a.getId());
                            eventDAO.createEvent(event);
                            session.setAttribute("notification", "Event created successfully!");
                            response.sendRedirect("event-manager");

                        }
                        case "update" -> {
                            int eventId = Integer.parseInt(request.getParameter("event_id"));
                            String title = request.getParameter("title");
                            String description = request.getParameter("description");
                            LocalDateTime startTime = LocalDateTime.parse(request.getParameter("start_time"), formatter);
                            LocalDateTime endTime = LocalDateTime.parse(request.getParameter("end_time"), formatter);
                            Event event = new Event(eventId, title, description, startTime, endTime, a.getId(), null, null);
                            eventDAO.updateEvent(event);
                            session.setAttribute("notification", "Event updated successfully!");
                            response.sendRedirect("event-manager");
                        }
                        case "delete" -> {
                            int eventId = Integer.parseInt(request.getParameter("event_id"));
                            eventDAO.deleteEvent(eventId);
                            session.setAttribute("notification", "Event deleted successfully!");
                            response.sendRedirect("event-manager");
                        }
                        case "register" -> {
                            int eventId = Integer.parseInt(request.getParameter("event_id"));
                            eventDAO.registerForEvent(eventId, a.getId());
                            session.setAttribute("notification", "Registered for event successfully!");
                            response.sendRedirect("event-manager");
                        }
                        case "updateStatus" -> {
                            int eventId = Integer.parseInt(request.getParameter("event_id"));
                            int registrationId = Integer.parseInt(request.getParameter("registration_id"));
                            String status = request.getParameter("status");
                            eventDAO.updateRegistrationStatus(registrationId, status);
                            session.setAttribute("notification", "Registration status updated successfully!");
                            response.sendRedirect("event-manager?action=viewRegistrations&event_id=" + eventId);
                        }
                        default -> {
                        }
                    }
                }
            } catch (Exception ex) {
                session.setAttribute("notificationErr", "Operation failed: " + ex.getMessage());
                ex.printStackTrace();
            }

        } else {
            response.sendRedirect("../login");
        }
    }
}
