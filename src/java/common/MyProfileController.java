/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package common;

import dal.AccountDAO;
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

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "MyProfileController", urlPatterns = {"/common/my-profile"})
public class MyProfileController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        AccountDAO accountDAO = new AccountDAO();
        Account a = (Account) session.getAttribute("account");
        if (a != null) {
            Account currentAccount = accountDAO.getByID(a.getId());
            System.out.println(currentAccount);
            request.setAttribute("currentAccount", currentAccount);
            request.getRequestDispatcher("my-profile.jsp").forward(request, response);
        } else {
            response.sendRedirect("../login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        AccountDAO accountDAO = new AccountDAO();
        StudentDAO studentDAO = new StudentDAO();
        LecturerDAO lecturerDAO = new LecturerDAO();
        Account a = (Account) session.getAttribute("account");
        if (a != null) {
            String action = request.getParameter("action");
            if (action.equals("edit-student")) {
                int aId = Integer.parseInt(request.getParameter("cid"));
                int sid = Integer.parseInt(request.getParameter("studentId"));
                // for account 
                String address = request.getParameter("address");
                String phone = request.getParameter("phone");
                // for student
                String fullName = request.getParameter("fullName");

                try {
                    accountDAO.updateAccount(aId, address, phone);
                    studentDAO.updateStudent(sid, fullName);
                    session.setAttribute("notification", "Profile update successfully! ");
                } catch (Exception e) {
                    session.setAttribute("notificationErr", "Profile update Faild: "+e.getMessage());
                }
                response.sendRedirect("my-profile");
            }
            if (action.equals("edit-lecturer")) {
                int aId = Integer.parseInt(request.getParameter("cid"));
                int lId = Integer.parseInt(request.getParameter("lId"));
                // for account 
                String address = request.getParameter("address");
                String phone = request.getParameter("phone");
                // for student
                String fullName = request.getParameter("fullName");

                try {
                    accountDAO.updateAccount(aId, address, phone);
                    lecturerDAO.updateLecturer(lId, fullName);
                    session.setAttribute("notification", "Profile update successfully! ");
                } catch (Exception e) {
                    session.setAttribute("notificationErr", "Profile update Faild: "+e.getMessage());
                }
                response.sendRedirect("my-profile");
            }
        }
    }
}
