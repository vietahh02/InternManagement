/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.lecturer;

import com.google.gson.Gson;
import dal.Dao;
import dal.LecturerDAO;
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
import model.Lecturer;
import model.Student;

/**
 *
 * @author Admin
 */
@WebServlet(name = "Schedule-lec", urlPatterns = {"/lecturer/schedule"})
public class Schedule extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Dao d = new Dao();
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        LecturerDAO ld = new LecturerDAO();
        Lecturer l = ld.getByAccountId(a.getId());
        request.setAttribute("amount", d.getAllClassesByLecturer(l.getLecturer_id()).size());
        request.getRequestDispatcher("schedule-lecturer.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Dao d = new Dao();
        String jsonString = null;
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        LecturerDAO ld = new LecturerDAO();
        Lecturer l = ld.getByAccountId(a.getId());

        if (request.getParameter("date") != null) {
            String date = request.getParameter("date");
            jsonString = new Gson().toJson(d.getAmountClassAtt(l.getLecturer_id(), date));

        }
        if (request.getParameter("date1") != null) {
            String date = request.getParameter("date1");
            jsonString = new Gson().toJson(d.getA(l.getLecturer_id(), date));

        }
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(jsonString);
    }

}
