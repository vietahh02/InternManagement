/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package common;

import dal.AccountDAO;
import dal.Dao;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.TypeSupport;

/**
 *
 * @author Admin
 */
@WebServlet(name = "SupportController", urlPatterns = {"/common/support"})
public class SupportController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        AccountDAO accountDAO = new AccountDAO();
        Account a = (Account) session.getAttribute("account");
        Dao d = new Dao();
        if (a != null) {
            Account currentAccount = accountDAO.getByID(a.getId());
            System.out.println(currentAccount);
            request.setAttribute("currentAccount", currentAccount);
            request.setAttribute("types", d.getTypes());
            request.setAttribute("list", d.getAllSupportByAccountID(a.getId()));
            request.getRequestDispatcher("support.jsp").forward(request, response);
        } else {
            response.sendRedirect("../login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        Dao d = new Dao();
        Account a = (Account) session.getAttribute("account");
        TypeSupport ts = new TypeSupport();
        model.Support s = new model.Support();
        try {
            ts.setType_id(Integer.parseInt(title));
            s.setContent(content);
            s.setAccount(a);
            s.setStatus("in progress");
            s.setTs(ts);
        } catch (Exception e) {
            System.out.println(e);
            response.sendRedirect("../login");
        }
        boolean check = d.addSupport(s);
        if (check) {
            session.setAttribute("notification", "Add support successfully! ");
            doGet(request, response);
        } else {
            session.setAttribute("notificationErr", "Fail!!!");
            doGet(request, response);
        }
    }

}
