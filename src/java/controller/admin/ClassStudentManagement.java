/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin;

import dal.StudentReportDAO;
import dal.UserClassDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import model.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@WebServlet(name = "ClassStudentController", urlPatterns = {"/admin/class-student"})
public class ClassStudentManagement extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        int cid = Integer.parseInt(request.getParameter("cid"));
        UserClassDAO ucdao = new UserClassDAO();
        if (a != null && a.getRoleAccount().getRole_id() == 1) {
            List<UserClass> uc = ucdao.getAllUserClasses(cid);
            List<Student> studentsNotInClass = ucdao.getStudentsNotInClass(cid);
            String action = request.getParameter("action");
            if (action != null && action.equals("export")) {
                System.out.println(action);
                String filePath = getServletContext().getRealPath("/") + "static/ClassUserList.xlsx";
                try (FileInputStream fis = new FileInputStream(new File(filePath))) {
                    Workbook workbook = new XSSFWorkbook(fis);
                    Sheet sheet = workbook.getSheetAt(0);

                    List<ClassStudentDTO> list = ucdao.getAllUserClasse(cid);
                    // Write data to the Excel file
                    int startRow = 1; // Row index starts at 0, so 1 means row 2
                    for (ClassStudentDTO ucs : list) {
                        Row row = sheet.getRow(startRow++);
                        row.getCell(0).setCellValue(ucs.getRollNumber());
                        row.getCell(1).setCellValue(ucs.getEmail());
                        row.getCell(2).setCellValue(ucs.getFullName());
                        row.getCell(3).setCellValue(ucs.getPhone());
                        row.getCell(4).setCellValue(ucs.getMajor());
                        row.getCell(5).setCellValue(ucs.getCompany()); // Trợ cấp
                        row.getCell(6).setCellValue(ucs.getJobTitle()); // Nhận xét
                        row.getCell(7).setCellValue(ucs.getLinkCV()); // kn
                    }
                    String newFilePath = getServletContext().getRealPath("/") + "static/student-class.xlsx";
                    try (FileOutputStream fos = new FileOutputStream(new File(newFilePath))) {
                        workbook.write(fos);
                    }
                    workbook.close();

                    // Prepare for download
                    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                    response.setHeader("Content-Disposition", "attachment; filename=student-class.xlsx");
                    try (FileInputStream downloadFis = new FileInputStream(new File(newFilePath)); OutputStream out = response.getOutputStream()) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = downloadFis.read(buffer)) != -1) {
                            out.write(buffer, 0, bytesRead);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            request.setAttribute("studentsNotInClass", studentsNotInClass);
            request.setAttribute("classList", uc);
            request.getRequestDispatcher("class-student.jsp").forward(request, response);
        } else {
            response.sendRedirect("../login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        if (a != null && a.getRoleAccount().getRole_id() == 1) {
            String action = request.getParameter("action");
            if (action.equals("add")) {
                String[] studentIds = request.getParameterValues("student_ids");
                int classId = Integer.parseInt(request.getParameter("class_id"));
                UserClassDAO ucdao = new UserClassDAO();

                if (studentIds != null) {
                    for (String studentId : studentIds) {
                        ucdao.addStudentToClass(Integer.parseInt(studentId), classId);
                    }
                }
                response.sendRedirect("class-student?cid=" + classId);
            }
            if (action.equals("delete")) {
                int userClassId = Integer.parseInt(request.getParameter("userClass_id"));
                int cid = Integer.parseInt(request.getParameter("cid"));
                UserClassDAO ucdao = new UserClassDAO();

                if (ucdao.canDeleteUserClass(userClassId)) {
                    ucdao.deleteUserClass(userClassId);
                    request.getSession().setAttribute("notification", "Student deleted successfully.");
                } else {
                    request.getSession().setAttribute("notificationErr", "Cannot delete student. Attendance records exist.");
                }

                response.sendRedirect("class-student?cid=" + cid);
            }

        } else {
            response.sendRedirect("../login");
        }
    }
}
