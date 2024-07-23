/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.student;

import com.google.gson.Gson;
import dal.Dao;
import dal.StudentDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.Student;

/**
 *
 * @author Admin
 */
@WebServlet(name = "Schedule-stu", urlPatterns = {"/student/schedule"})
public class Schedule extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("schedule-student.jsp").forward(request, response);
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
