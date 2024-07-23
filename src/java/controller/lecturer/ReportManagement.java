package controller.lecturer;

import controller.lecturer.*;
import dal.AccountDAO;
import dal.ClasssDAO;
import dal.LecturerDAO;
import dal.QuestionDAO;
import dal.ReportDAO;
import dal.StudentDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import model.*;

/**
 *
 * @author ADMIN
 */
@MultipartConfig
@WebServlet(name = "ReportManagement", urlPatterns = {"/admin/report"})
public class ReportManagement extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        ReportDAO reportDAO = new ReportDAO();
        ClasssDAO classsDAO = new ClasssDAO();
        if (a != null && a.getRoleAccount().getRole_id() == 1) {
            String type = request.getParameter("type");
            String classParam = request.getParameter("cid");
             List<model.Class> listClass = classsDAO.getAllClass();
            Integer class_id = (classParam != null && !classParam.isEmpty()) ? Integer.valueOf(classParam) : null;
            List<Report> reports = reportDAO.getAllReportForAdmin(type, class_id);
            request.setAttribute("questions", reports);
            request.setAttribute("listClass", listClass);

            request.getRequestDispatcher("report.jsp").forward(request, response);
        } else {
            response.sendRedirect("../login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
