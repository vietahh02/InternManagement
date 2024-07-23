/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.lecturer;

import com.google.gson.Gson;
import dal.Dao;
import dal.LecturerDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import model.Account;
import model.Lecturer;

/**
 *
 * @author Admin
 */
@WebServlet(name = "ViewAttendance", urlPatterns = {"/lecturer/viewAttendance"})
public class ViewAttendance extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String classId = request.getParameter("classId");
        String who = request.getParameter("who");
        String date = request.getParameter("date");
        if ("lec".equals(who)) {
            Dao d = new Dao();
            String jsonString;
            if (date.equals(getDateNow())) {
                HttpSession session = request.getSession();
                Account a = (Account) session.getAttribute("account");
                LecturerDAO ld = new LecturerDAO();
                Lecturer l = ld.getByAccountId(a.getId());
                jsonString = new Gson().toJson(d.getStudentInClass(l.getLecturer_id(), classId, d.checkAttendanceDate(Integer.parseInt(classId))));
            } else {
                jsonString = new Gson().toJson(d.getAttendentForLecturer(date, classId));
            }
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(jsonString);
        } else {

        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private String getDateNow() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        LocalDate localDate = LocalDate.now();
        return dtf.format(localDate);
    }

}
