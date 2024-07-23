/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.lecturer;

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
import model.Account;

/**
 *
 * @author Admin
 */
@WebServlet(name="ViewClass", urlPatterns={"/lecturer/viewClass"})
public class ViewClass extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        Dao d = new Dao();
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        LecturerDAO ld = new LecturerDAO();
        if(a == null) {
            response.sendRedirect("/pro/login");
            return;
        }
        request.setAttribute("classList", d.getAllClassesByLecturer(ld.getByAccountId(a.getId()).getLecturer_id()));
        request.getRequestDispatcher("view-class.jsp").forward(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
    }

}