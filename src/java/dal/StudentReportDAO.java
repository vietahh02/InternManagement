/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Student;
import model.StudentReport;

/**
 *
 * @author ADMIN
 */
public class StudentReportDAO extends DBContext {

    public List<StudentReport> getAllStudentsByClassId(Integer classId, String type) {
        List<StudentReport> students = new ArrayList<>();
        StudentDAO studentDAO = new StudentDAO();
        LecturerDAO lecturerDAO = new LecturerDAO();
        ReportDAO reportDAO = new ReportDAO();

        try {
            String query = """
                           SELECT s.*,l.*, r.id as rid FROM Student s
            JOIN userClass uc ON s.student_id = uc.student_id
            JOIN Report r on r.student_report = s.account_id
            JOIN Class c on uc.class_id = c.class_id
            JOIN Lecturer l on l.lecturer_id = c.lecturer_id 
                           where uc.class_id = ? and r.type = ?
            """;

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, classId);
            preparedStatement.setString(2, type);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    StudentReport student = new StudentReport();
                    student.setStudent(studentDAO.getById(rs.getInt("student_id")));
                    student.setLecturer(lecturerDAO.getById(rs.getInt("lecturer_id")));
                    student.setReport(reportDAO.getByID(rs.getInt("rid")));
                    // Add other student fields as needed
                    students.add(student);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public static void main(String[] args) {
        StudentReportDAO s = new StudentReportDAO();
        List<StudentReport> l = s.getAllStudentsByClassId(1011, "Final");
        l.forEach(System.out::println);
    }
}
