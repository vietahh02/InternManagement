/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.lecturer;

import dal.CommentDAO;
import dal.LecturerDAO;
import dal.TaskDAO;
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
import model.Comment;
import model.Task;

@WebServlet(name = "TaskDetailController", urlPatterns = {"/lecturer/task-detail"})
public class TaskDetailController extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        TaskDAO taskDAO = new TaskDAO();
        CommentDAO commentDAO = new CommentDAO();
        int id = Integer.parseInt(request.getParameter("id"));

        // Kiểm tra xem người dùng đã đăng nhập và có quyền là giảng viên hay không
        if (a != null && a.getRoleAccount().getRole_id() == 3) {
            Task task = taskDAO.getById(id);
            List<Comment> listComments = commentDAO.getAllCCommetOfTask(id);
            request.setAttribute("task", task);
            request.setAttribute("comments", listComments);
            request.getRequestDispatcher("task-detail.jsp").forward(request, response);
        } // Nếu người dùng chưa đăng nhập hoặc không có quyền, chuyển hướng tới trang đăng nhập
        else {
            response.sendRedirect("../login");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        
        LecturerDAO lecturerDAO = new LecturerDAO();
        CommentDAO commentDAO = new CommentDAO();
        String action = request.getParameter("action");
        TaskDAO taskDAO = new TaskDAO();
        int tid = Integer.parseInt(request.getParameter("tid"));
        // Kiểm tra xem người dùng đã đăng nhập và có quyền là giảng viên hay không
        if (a != null && a.getRoleAccount().getRole_id() == 3) {
            int lid = lecturerDAO.getByAccountId(a.getId()).getLecturer_id();
            // Nếu hành động là thêm bình luận
            if (action.equals("add-comment")) {
                // Lấy nội dung comment từ request
                String comment = request.getParameter("comment");
                try {
                    // Thêm comment mới vào cơ sở dữ liệu
                    commentDAO.addLecturerComment(tid, lid, comment);
                    // Đặt thông báo thành công vào session
                    session.setAttribute("notification", "Add comment success!");
                    // Chuyển hướng tới trang task detail
                    response.sendRedirect("task-detail?id=" + tid);
                } catch (Exception e) {
                    session.setAttribute("notificationErr", e.getMessage());
                    response.sendRedirect("task-detail?id=" + tid);
                }
                
            }
            // Nếu hành động là chỉnh sửa bình luận
            if (action.equals("edit-comment")) {
                try {
                    String comment = request.getParameter("comment");
                    int id = Integer.parseInt(request.getParameter("cid"));
                    commentDAO.updateComment(id, comment);
                    session.setAttribute("notification", "Edit comment success!");
                    response.sendRedirect("task-detail?id=" + tid);
                } catch (Exception e) {
                    session.setAttribute("notificationErr", e.getMessage());
                    response.sendRedirect("task-detail?id=" + tid);
                }
            }
            if (action.equals("grade-task")) {
                float grade = Float.parseFloat(request.getParameter("grade"));
                
                if(grade < 0 || grade > 10){
                    session.setAttribute("notificationErr", "Gradde must be between 0 and 10");
                    response.sendRedirect("task-detail?id=" + tid);
                    return;
                }
                
                Task currentTask = taskDAO.getById(tid);
                if (grade == currentTask.getGrade()) {
                    response.sendRedirect("task-detail?id=" + tid);
                    return;
                }
                try {
                    taskDAO.gradeTask(tid, grade);
                    session.setAttribute("notification", "Grade task success!");
                    response.sendRedirect("task-detail?id=" + tid);
                } catch (Exception e) {
                    session.setAttribute("notificationErr", e.getMessage());
                    response.sendRedirect("task-detail?id=" + tid);
                }
            }
        } // Nếu người dùng chưa đăng nhập hoặc không có quyền, chuyển hướng tới trang đăng nhập
        else {
            response.sendRedirect("../login");
        }
    }
}
