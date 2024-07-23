package controller.admin;

import dal.DashboardDAO;
import java.io.IOException;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "Dashboard", urlPatterns = {"/admin/dashboard"})
public class Dashboard extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DashboardDAO dao = new DashboardDAO();

        Map<String, Integer> taskCounts = dao.getTaskCounts();
        Map<String, Integer> roleCounts = dao.getRoleCounts();
        int totalPeople = dao.getTotalPeople();
        Map<String, Integer> classesForLecturers = dao.getClassesForLecturers();
        Map<String, Integer> passFailCounts = dao.getPassFailCounts();
        int newsCount = dao.getNewsCount();

        request.setAttribute("taskCounts", taskCounts);
        request.setAttribute("roleCounts", roleCounts);
        request.setAttribute("totalPeople", totalPeople);
        request.setAttribute("classesForLecturers", classesForLecturers);
        System.out.println(classesForLecturers);
        request.setAttribute("passFailCounts", passFailCounts);
        request.setAttribute("newsCount", newsCount);

        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
