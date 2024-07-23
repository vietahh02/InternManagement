package controller.student;

import dal.EventDAO;
import model.Event;
import model.EventRegistration;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;

@WebServlet(name = "StudentEventServlet", urlPatterns = {"/student/events"})
public class StudentEventServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        EventDAO eventDAO = new EventDAO();
        if (a != null && a.getRoleAccount().getRole_id() == 2) {
            try {
                List<Event> events = eventDAO.getAllEvents();
                List<EventRegistration> registrations = eventDAO.getRegistrationsByUser(a.getId());
                request.setAttribute("events", events);
                request.setAttribute("registrations", registrations);
                request.getRequestDispatcher("student-events.jsp").forward(request, response);
            } catch (Exception ex) {

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
        if (a != null && a.getRoleAccount().getRole_id() == 2) {
            String action = request.getParameter("action");
            try {
                if ("register".equals(action)) {
                    int eventId = Integer.parseInt(request.getParameter("event_id"));
                    List<EventRegistration> registrations = eventDAO.getRegistrationsByUser(a.getId());

                    boolean alreadyRegistered = registrations.stream()
                            .anyMatch(reg -> reg.getEventId() == eventId);

                    if (alreadyRegistered) {
                        session.setAttribute("notificationErr", "You are already registered for this event.");
                    } else {
                        eventDAO.registerForEvent(eventId, a.getId());
                        session.setAttribute("notification", "Registered for event successfully!");
                    }
                }
            } catch (Exception ex) {
                session.setAttribute("notificationErr", "Operation failed: " + ex.getMessage());
                ex.printStackTrace();
            }
            response.sendRedirect("events");
        } else {
            response.sendRedirect("../login");
        }
    }
}