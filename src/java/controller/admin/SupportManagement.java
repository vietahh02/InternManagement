/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin;

import dal.ClasssDAO;
import dal.Dao;
import dal.ReportDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Account;
import model.Report;
import model.Support;

/**
 *
 * @author Admin
 */
@WebServlet(name = "SupportManagement", urlPatterns = {"/admin/support"})
public class SupportManagement extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        Dao d = new Dao();
        if (a != null && a.getRoleAccount().getRole_id() == 1) {
            request.setAttribute("listSupport", d.getAllSupport());
            request.getRequestDispatcher("support.jsp").forward(request, response);
        } else {
            response.sendRedirect("../login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String status = request.getParameter("status");
        String note = request.getParameter("note");
        String id = request.getParameter("id");
        Account a = (Account) session.getAttribute("account");
        Support s = new Support();
        Dao d = new Dao();
        try {
            int idReal = Integer.parseInt(id);
            s.setSuport_id(idReal);
            s.setProcess_note(note);
            if("1".equals(status)) {
                status = "approved";
            }else {
                status = "rejected";
            }
            s.setStatus(status);
            s.setAccount(a);
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
        boolean check = d.answerSupport(s);
        if (check) {
            session.setAttribute("notification", "Answer support successfully! ");
            doGet(request, response);
        } else {
            session.setAttribute("notificationErr", "Fail!!!");
            doGet(request, response);
        }
    }

}
