package controller.student;


import dal.DocumentDAO;
import dal.LecturerDAO;
import dal.StudentDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.Document;
import model.Lecturer;
import model.Student;

@WebServlet(name = "DocumentController", urlPatterns = {"/student/document-manager"})
public class DocumentController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        DocumentDAO documentDAO = new DocumentDAO();
        if (a != null && a.getRoleAccount().getRole_id() == 2) {
            try {
                LecturerDAO lecturerDAO = new LecturerDAO();
                StudentDAO studentDAO = new StudentDAO();
                Student student = studentDAO.getByAccountId(a.getId());
                Lecturer lecturer = lecturerDAO.getLecturerOfStudent(student.getStudent_id());
                List<Document> listDocument = documentDAO.getAllDocuments(lecturer.getAccountLecturer().getId());
                request.setAttribute("listDocument", listDocument);
                request.getRequestDispatcher("document-manager.jsp").forward(request, response);
            } catch (SQLException ex) {
               
            }
        } else {
            response.sendRedirect("../login");
        }
    }
}
