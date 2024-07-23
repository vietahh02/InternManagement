/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.student;

import com.google.gson.Gson;
import dal.ClasssDAO;
import dal.Dao;
import dal.ObjectiveDAO;
import dal.StudentDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import model.Account;
import model.Objective;
import model.Student;

/**
 *
 * @author Admin
 */
@WebServlet(name = "AttendanceReport", urlPatterns = {"/student/attendanceReport"})
public class AttendanceReport extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        StudentDAO studentDAO = new StudentDAO();
        Dao d = new Dao();
        if (a != null && a.getRoleAccount().getRole_id() == 2) {
            Student student = studentDAO.getByAccountId(a.getId());
            ArrayList<String> dates = d.getMonthAndYear(student.getStudent_id());
            request.setAttribute("das", dates);
            request.setAttribute("list", d.getAttendentForIntern(student.getStudent_id(), dates.get(0)));
            request.getRequestDispatcher("adttendance-report.jsp").forward(request, response);
        } else {
            response.sendRedirect("../login");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String date = request.getParameter("date");
        Dao d = new Dao();
        String jsonString;
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        StudentDAO studentDAO = new StudentDAO();
        Student student = studentDAO.getByAccountId(a.getId());
        jsonString = new Gson().toJson(d.getAttendentForIntern(student.getStudent_id(), date));
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(jsonString);
    }
}
