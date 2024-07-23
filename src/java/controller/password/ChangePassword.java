/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.password;

import dal.Dao;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Admin
 */
@WebServlet(name = "ChangePassword", urlPatterns = {"/changePassword"})
public class ChangePassword extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("change-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String old = request.getParameter("old");
        String newP = request.getParameter("newP");
        String conP = request.getParameter("conP");
        String user = null;
        String err = null;
        String suc = null;
        Dao d = new Dao();
        if (!d.checkPassword(newP)) {
            err = "Password must be 8-20 characters have most less 1 number, 1 letter, 1 capital letter, 1 special character!!!";
        } else if (!newP.equalsIgnoreCase(conP)) {
            err = "New password and confirm password not match!!!";
        } else {
            HttpSession session = request.getSession();
            if (session.getAttribute("acc") != null) {
                user = session.getAttribute("acc").toString().split("\\|")[0];
            } else {
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equals("acc")) {
                            user = cookie.getValue().split("\\|")[0];
                            break;
                        }
                    }
                }
            }

            if (user == null) {
                response.sendRedirect("login");
            } else {
                if (!d.checkAccount(user, old)) {
                    err = "Old password incorrect!!!";
                } else {
                    d.setNewPassword(user, newP);
                    suc = "Change password successfully!!!";
                }
            }
        }

        request.setAttribute("err", err);
        request.setAttribute("suc", suc);
        request.getRequestDispatcher("change-password.jsp").forward(request, response);
    }
}