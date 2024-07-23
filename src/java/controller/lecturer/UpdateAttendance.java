/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.lecturer;

import dal.Dao;
import dal.LecturerDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.Lecturer;

/**
 *
 * @author Admin
 */
@WebServlet(name="UpdateAttendance", urlPatterns={"/lecturer/updateAttendance"})
public class UpdateAttendance extends HttpServlet {
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String cid = request.getParameter("cid");
        Dao d = new Dao();
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        LecturerDAO ld = new LecturerDAO();
        Lecturer l = ld.getByAccountId(a.getId());
        request.setAttribute("list", d.getStudentInClass(l.getLecturer_id(), cid, "1"));
        request.setAttribute("run", "up");
        session.setAttribute("classId", cid);
        request.getRequestDispatcher("attendance.jsp").forward(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        HttpSession session = request.getSession();
        String classId = session.getAttribute("classId").toString();
        String[] arrRoll = {};
        if (request.getParameterValues("att") != null) {
            arrRoll = request.getParameterValues("att");
        }
        Dao d = new Dao();
        boolean done = d.updateAttendance(arrRoll, classId);
        if (done) {
            session.setAttribute("notification", "Update attendance successfully!");
        } else {
            session.setAttribute("notificationErr", "Error!!!");
        }
        response.sendRedirect("viewClass");
    }

}