package controller.lecturer;

import dal.AccountDAO;
import dal.LecturerDAO;
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
@WebServlet(name = "QuestionLecturerController", urlPatterns = {"/lecturer/question"})
public class QuestionLecturerController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        QuestionDAO questionDAO = new QuestionDAO();
        System.out.println(a);
        if (a != null && a.getRoleAccount().getRole_id() == 3) {
            List<Question> questions = questionDAO.getAllQuestionLecturer(a.getId());
            request.setAttribute("questions", questions);
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
        if (a != null && a.getRoleAccount().getRole_id() == 3) {
            int id = Integer.parseInt(request.getParameter("id"));

            String answer = request.getParameter("answer");

            Question question = questionDAO.getQuestionById(id);
            question.setReply(answer);
            question.setStatus("Answered");
            question.setUpdated_at(LocalDateTime.now());

            try {
                questionDAO.updateNews(question);
                session.setAttribute("notification", "Reply successfully!");
            } catch (Exception e) {
                session.setAttribute("notificationErr", "Failed to Reply question: " + e.getMessage());
            }
            response.sendRedirect("question");

        } else {
            response.sendRedirect("../login");
        }

    }
}
