package controller.lecturer;

import dal.AccountDAO;
import dal.ClasssDAO;
import dal.LecturerDAO;
import dal.QuestionDAO;
import dal.ReportDAO;
import dal.StudentDAO;
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
@WebServlet(name = "ReportController", urlPatterns = {"/lecturer/report"})
public class ReportController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        ReportDAO reportDAO = new ReportDAO();
        LecturerDAO ldao = new LecturerDAO();
        ClasssDAO classsDAO = new ClasssDAO();
        if (a != null && a.getRoleAccount().getRole_id() == 3) {
            Lecturer lecturer = ldao.getByAccountId(a.getId());
            List<model.Class> listClassOfLec = classsDAO.getAllClassByLecturer(lecturer.getLecturer_id());
            String type = request.getParameter("type");
            String classParam = request.getParameter("cid");
            Integer class_id = (classParam != null && !classParam.isEmpty()) ? Integer.valueOf(classParam) : null;
            List<Report> questions = reportDAO.getAllReportForLecturer(a.getId(), type, class_id);
            request.setAttribute("questions", questions);
            request.setAttribute("classes", listClassOfLec);
            request.getRequestDispatcher("report.jsp").forward(request, response);
        } else {
            response.sendRedirect("../login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        ReportDAO reportDAO = new ReportDAO();
        AccountDAO accountDAO = new AccountDAO();
        StudentDAO sdao = new StudentDAO();

        if (a != null && a.getRoleAccount().getRole_id() == 3) {
            String action = request.getParameter("action");
            if (action.equals("add")) {
                int sid = Integer.parseInt(request.getParameter("sid"));
                int cid = Integer.parseInt(request.getParameter("cid"));
                Account studentAcc = sdao.getById(sid).getAccount();
                String content = request.getParameter("content");
                String type = request.getParameter("type");
                float knowledge = Float.parseFloat(request.getParameter("knowledge"));
                float soft_skill = Float.parseFloat(request.getParameter("soft_skill"));
                float attitude = Float.parseFloat(request.getParameter("attitude"));
                StudentDAO studentDAO = new StudentDAO();
                Student studentAccount = studentDAO.getById(sid);

                Report report = new Report();
                report.setContent(content);
                report.setType(type);
                report.setKnowledge(knowledge);
                report.setAttitude(attitude);
                report.setSoft_skill(soft_skill);
                float final_grade = (float) ((knowledge * 0.4) + (attitude * 0.3) + (soft_skill * 0.3));
                report.setFinal_grade(final_grade);
                report.setSender(a);

                report.setStudent_report(studentAccount.getAccount());
                boolean isExsited = reportDAO.reportExists(studentAccount.getAccount().getId(), type);
                try {
                    if (isExsited) {
                        session.setAttribute("notificationErr", "You already report " + type + " for this student!");
                    } else {
                        reportDAO.addReport(report);
                        if (report.getFinal_grade() < 4 && report.getType().equals("Final")) {
                            accountDAO.changeStatus(studentAcc.getId(), "inactive");
                        }
                        session.setAttribute("notification", "Report successfully!");
                    }
                } catch (Exception e) {
                    session.setAttribute("notificationErr", "Failed to Reply question: " + e.getMessage());
                }
                response.sendRedirect("class-student?cid=" + cid);
            }
            if (action.equals("update")) {
                int id = Integer.parseInt(request.getParameter("id"));
                StudentDAO studentDAO = new StudentDAO();
                String content = request.getParameter("content");
                float knowledge = Float.parseFloat(request.getParameter("knowledge"));
                float soft_skill = Float.parseFloat(request.getParameter("soft_skill"));
                float attitude = Float.parseFloat(request.getParameter("attitude"));
                float final_grade = (float) ((knowledge * 0.4) + (attitude * 0.3) + (soft_skill * 0.3));

                try {
                    reportDAO.updateReport(content, knowledge, soft_skill, attitude, final_grade, id);
                    Report report = reportDAO.getByID(id);
                    
                    if (report.getFinal_grade() < 4 && report.getType().equals("Final")) {
                          accountDAO.changeStatus(report.getStudent_report().getId(), "inactive");
                    } else if (report.getFinal_grade() >= 4 && report.getType().equals("Final")) {
                        accountDAO.changeStatus(report.getStudent_report().getId(), "active");
                    }
                    session.setAttribute("notification", "Report update successfully!");
                } catch (Exception e) {
                    session.setAttribute("notificationErr", "Failed to update: " + e.getMessage());
                }
                response.sendRedirect("report");
            }

        } else {
            response.sendRedirect("../login");
        }

    }
}
