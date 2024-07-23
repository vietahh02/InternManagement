/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.lecturer;

import dal.TaskDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Account;
import model.Task;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "TaskManagement", urlPatterns = {"/lecturer/task"})
public class TaskManagement extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        TaskDAO taskDAO = new TaskDAO();
        String status = request.getParameter("status");
        int oId = Integer.parseInt(request.getParameter("oid"));

        if (a != null && a.getRoleAccount().getRole_id() == 3) {
            taskDAO.updateTaskStatuses();
            List<Task> task = taskDAO.getAllTaskyObject(oId, status);
            request.setAttribute("task", task);
            request.getRequestDispatcher("task.jsp").forward(request, response);
        } else {
            response.sendRedirect("../login");
        }

    }

   @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        TaskDAO taskDAO = new TaskDAO();

        String action = request.getParameter("action");
        // Kiểm tra xem người dùng đã đăng nhập và có quyền là giảng viên hay không
        if (a != null && a.getRoleAccount().getRole_id() == 3) {
            // Nếu hành động là ADD
            if (action.equals("add")) {
                int oId = Integer.parseInt(request.getParameter("oid"));
                String title = request.getParameter("title");
                String description = request.getParameter("description");
                String start_date = request.getParameter("start_date");
                String end_date = request.getParameter("end_date");
                String status = "not started";
                try {
                    taskDAO.addTask(title, description, status, start_date, end_date, oId);
                    session.setAttribute("notification", "Task add successfully!");
                    response.sendRedirect("task?oid=" + oId);
                } 
                catch (Exception e) {
                    session.setAttribute("notificationErr", e.getMessage());
                    response.sendRedirect("task?oid=" + oId);
                }
            }
            
            //Nếu hành động là DELETE
            if (action.equals("delete")) {
                int id = Integer.parseInt(request.getParameter("id"));
                int oId = Integer.parseInt(request.getParameter("oid"));

                boolean isTaskInUse = taskDAO.isTaskInUse(id);
                if (!isTaskInUse) {
                    try {
                        taskDAO.deleteTask(id);
                        session.setAttribute("notification", "Task delete successfully!");
                        response.sendRedirect("task?oid=" + oId);
                    } 
                    catch (Exception e) {
                        session.setAttribute("notificationErr", e.getMessage());
                        response.sendRedirect("task?oid=" + oId);
                    }
                } 
                else {
                    session.setAttribute("notificationErr", "Delete failed: This task started!");
                    response.sendRedirect("task?oid=" + oId);
                }
            }
            
            //Nếu hành động là UPDATE
            if (action.equals("update")) {
                int oId = Integer.parseInt(request.getParameter("oid"));
                int id = Integer.parseInt(request.getParameter("id"));
                String title = request.getParameter("title");
                String description = request.getParameter("description");
                String start_date = request.getParameter("start_date");
                String end_date = request.getParameter("end_date");
                try {
                    taskDAO.updateTask(title, description, start_date, end_date, id);
                    session.setAttribute("notification", "Task update successfully!");
                    response.sendRedirect("task?oid=" + oId);
                } 
                catch (Exception e) {
                    session.setAttribute("notificationErr", e.getMessage());
                    response.sendRedirect("task?oid=" + oId);
                }
            }
        } 
        else {
            response.sendRedirect("../login");
        }
    }

}
