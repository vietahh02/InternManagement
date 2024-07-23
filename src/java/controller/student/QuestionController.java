package controller.student;

import dal.AccountDAO;
import dal.LecturerDAO;
import dal.NewDAO;
import dal.QuestionDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import model.*;

/**
 *
 * @author ADMIN
 */
@MultipartConfig
@WebServlet(name = "QuestionController", urlPatterns = {"/student/question"})
public class QuestionController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        QuestionDAO questionDAO = new QuestionDAO();
        if (a != null && a.getRoleAccount().getRole_id() == 2) {
            List<Question> questions = questionDAO.getAllQuestionForStudent(a.getId());
            LecturerDAO lecturerDAO = new LecturerDAO();
            int lid = questionDAO.getTeacherIdOfStudent(a.getId());
            Lecturer lecturer = lecturerDAO.getById(lid);
            System.out.println(lecturer);
            request.setAttribute("questions", questions);
            request.setAttribute("lecturer", lecturer);
            request.getRequestDispatcher("question.jsp").forward(request, response);
        } else {
            response.sendRedirect("../login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        QuestionDAO questionDAO = new QuestionDAO();
        if (a != null && a.getRoleAccount().getRole_id() == 2) {
            String action = request.getParameter("action");
            if ("add".equals(action)) {
                // Get parameters from the form
                String title = request.getParameter("title");
                String content = request.getParameter("content");
                int receiverId = Integer.parseInt(request.getParameter("receiver"));

                // Validate parameters (optional, depending on your needs)
                if (title == null || title.trim().isEmpty() || content == null || content.trim().isEmpty()) {
                    session.setAttribute("notificationErr", "Title and content are required.");
                    response.sendRedirect("question");
                    return;
                }

                // Create Question object
                Question question = new Question();
                question.setTitle(title);
                question.setContent(content);
                question.setCreated_at(LocalDateTime.now());
                question.setUpdated_at(null);
                question.setSender(a);
                question.setReceiver(new AccountDAO().getByID(receiverId));
                try {
                    questionDAO.addQuestion(question);
                    session.setAttribute("notification", "Question add successfully! ");
                } catch (Exception e) {
                    session.setAttribute("notificationErr", "Question add failed! " + e.getMessage());
                }

                response.sendRedirect("question");
            } else if ("update".equals(action)) {
                // Get parameters from the form
                int id = Integer.parseInt(request.getParameter("id"));
                String title = request.getParameter("title");
                String content = request.getParameter("content");
                int receiverId = Integer.parseInt(request.getParameter("receiver"));

                // Validate parameters
                if (title == null || title.trim().isEmpty() || content == null || content.trim().isEmpty()) {
                    session.setAttribute("notificationErr", "Title and content are required.");
                    response.sendRedirect("question");
                    return;
                }
                Question question = questionDAO.getQuestionById(id);
                question.setTitle(title);
                question.setContent(content);
                question.setUpdated_at(null);
                question.setStatus("Processing");
                question.setReply(null);
                try {
                    questionDAO.updateNews(question);
                    session.setAttribute("notification", "Question updated successfully!");
                } catch (Exception e) {
                    session.setAttribute("notificationErr", "Failed to update question: " + e.getMessage());
                }
                response.sendRedirect("question");
            }
        } else {
            response.sendRedirect("../login");
        }

    }
}
